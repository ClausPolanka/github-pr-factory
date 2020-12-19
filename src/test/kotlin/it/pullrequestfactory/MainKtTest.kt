package it.pullrequestfactory

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.main
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.lang.System.lineSeparator
import java.nio.file.Files
import java.nio.file.Paths

class MainKtTest {

    private val CANDIDATE = "Radek-Leifer"
    private val AUTH_TOKEN = "any-valid-token"
    private val PAIRING_PARTNER = "claus-berni-dominik-christian-shubi-markus-mihai"
    private val PROPS_FILE_NAME = "app.properties"
    private val PROPS_FILE_PATH = "target/test-classes/$PROPS_FILE_NAME"
    private val WIRE_MOCK_DEFAULT_URL = "http://localhost:8080"
    private val REPO_PATH = "/repos/ClausPolanka/wordcount"
    private val LINK_HEADER = "<https://api.github.com/repositories/157517927/branches?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/branches?page=9>; rel=\"last\""
    private val PULL_REQUEST_PATH = "/repos/ClausPolanka/wordcount/pulls"
    private val PULL_REQUEST_LINK_HEADER_PAGE_1 = "<https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"last\""
    private val PULL_REQUEST_LINK_HEADER_PAGE_2 = "<https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"prev\", <https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"first\""
    private val PROJECT_VERSION = "1.0-SNAPSHOT"

    private val systemIn = System.`in`
    private val systemOut = System.out
    private val uiOutput = ByteArrayOutputStream()

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    @Before
    fun setUp() {
        createPropertyFileWith(props = listOf(
                "baseUrl=$WIRE_MOCK_DEFAULT_URL",
                "repoPath=$REPO_PATH",
                "projectVersion=$PROJECT_VERSION"))
        System.setOut(PrintStream(uiOutput))
    }

    @After
    fun tearDown() {
        reset()
        System.setIn(systemIn)
        System.setOut(systemOut)
        Files.deleteIfExists(Paths.get(PROPS_FILE_PATH))
    }

    @Test
    fun shows_version_output() {
        main(args = arrayOf("--version"))
        assertThat(uiOutput.toString()).contains("github-pr-factory version $PROJECT_VERSION")
    }

    @Test
    fun shows_help_output() {
        main(args = arrayOf("--help"))
        assertThat(uiOutput.toString()).contains("Usage: github-pr-factory [OPTION] COMMAND")
    }

    @Test
    fun creates_pull_requests_for_the_given_program_arguments() {
        stubRateLimit()
        stubGetRequestsForGithubBranchesFromFiles()

        main(args = arrayOf("open", "-c", CANDIDATE, "-g", AUTH_TOKEN, "-p", PAIRING_PARTNER))

        verifyPostRequestsToGithubToCreatePullRequests(CANDIDATE)
    }

    @Test
    fun shows_open_command_usage_when_arguments_are_not_correct() {
        main(args = arrayOf("open", "-c", CANDIDATE, "-g", AUTH_TOKEN, PAIRING_PARTNER))

        assertThat(uiOutput.toString()).contains("Usage: github-pr-factory open [OPTION]")
    }

    @Test
    fun closes_pull_requests_for_given_program_arguments() {
        stubRateLimit()
        val prs = stubGetRequestForPullRequests(CANDIDATE)

        main(args = arrayOf("close", "-c", CANDIDATE, "-g", AUTH_TOKEN))

        verifyPatchRequestToCloseOpenPullRequests(prs)
    }

    @Test
    fun shows_close_command_usage_when_arguments_are_not_correct() {
        main(args = arrayOf("close", "-c", CANDIDATE, "-g"))

        assertThat(uiOutput.toString()).contains("Usage: github-pr-factory close [OPTION]")
    }

    private fun verifyPatchRequestToCloseOpenPullRequests(prs: List<GetPullRequest>) {
        prs.forEach {
            verifyPatchRequestToCloseOpenPullRequestsFor(prNumber = it.number)
        }
    }

    private fun verifyPatchRequestToCloseOpenPullRequestsFor(prNumber: Int) {
        verify(patchRequestedFor(urlMatching("$PULL_REQUEST_PATH/$prNumber"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .addCommonHeaders())
    }

    private fun stubGetRequestsForGithubBranchesFromFiles() {
        (1..9).forEach {
            stubForGithubBranchesRequestPage(it)
        }
    }

    private fun stubRateLimit() {
        stubFor(get("/rate_limit").willReturn(aResponse().withStatus(200)
                .withBody("{ \n  \"rate\":  {\n    \"limit\": 5000,\n    \"used\": 0,\n    \"remaining\": 5000,\n    \"reset\": 1608411669\n  }\n}")))
    }

    private fun stubForGithubBranchesRequestPage(pageNr: Int) {
        stubFor(get("/repos/ClausPolanka/wordcount/branches?page=$pageNr")
                .willReturn(aResponse().withStatus(200)
                        // Only important for first request
                        .withHeader("Link", LINK_HEADER)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(File("json/branches-page-$pageNr.json").readText())))
    }

    private fun verifyPostRequestsToGithubToCreatePullRequests(candidate: String) {
        verify(8, postRequestedFor(urlEqualTo(PULL_REQUEST_PATH)))

        val candidateFirstName = candidate.split("-")[0]
        val candidateLastName = candidate.split("-")[1]

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 1 / Session 1 Claus",
                _base = Branch("master"),
                _head = Branch("radek_leifer_interation_1_claus")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 1 / Session 2 Berni",
                _base = Branch("radek_leifer_interation_1_claus"),
                _head = Branch("radek_leifer_interation_1_berni")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 1 / Session 3 Dominik [PR]",
                _base = Branch("radek_leifer_interation_1_berni"),
                _head = Branch("radek_leifer_iteration_1_dominik")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 2 / Session 3 Dominik",
                _base = Branch("radek_leifer_iteration_1_dominik"),
                _head = Branch("radek_leifer_iteration_2_dominik")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 2 / Session 4 Christian [PR]",
                _base = Branch("radek_leifer_iteration_2_dominik"),
                _head = Branch("radek_leifer_iteration_2_christian")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 3 / Session 5 Shubhi",
                _base = Branch("radek_leifer_iteration_2_christian"),
                _head = Branch("radek_leifer_iteration_3_shubi")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 3 / Session 6 Markus",
                _base = Branch("radek_leifer_iteration_3_shubi"),
                _head = Branch("radek_leifer_iteration_3_markus")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "$candidateFirstName $candidateLastName Iteration 3 / Session 7 Mihai",
                _base = Branch("radek_leifer_iteration_3_markus"),
                _head = Branch("radek_leifer_iteration_3_mihai")))
    }

    private fun verifyPostRequestToGithubToCreatePullRequestFor(pr: PullRequest) {
        println()
        println(pr)
        println()
        verify(postRequestedFor(urlMatching(PULL_REQUEST_PATH))
                .withRequestBody(matching(Regex.escape(Klaxon().toJsonString(pr))))
                .addCommonHeaders())
    }

    private fun createPropertyFileWith(props: List<String>) {
        Files.write(Paths.get(PROPS_FILE_PATH), props.joinToString(lineSeparator()).toByteArray())
    }

    private fun stubGetRequestForPullRequests(candidate: String): List<GetPullRequest> {
        val candidateFirstName = candidate.split("-")[0]
        val candidateLastName = candidate.split("-")[1]
        val pr1 = GetPullRequest(1, "$candidateFirstName $candidateLastName Iteration 1 / Session 1 Claus [PR]")
        val pr2 = GetPullRequest(2, "$candidateFirstName $candidateLastName Iteration 2 / Session 1 Claus")
        stubGetRequestForPullRequests(pr1, PULL_REQUEST_LINK_HEADER_PAGE_1, 1)
        stubGetRequestForPullRequests(pr2, PULL_REQUEST_LINK_HEADER_PAGE_2, 2)
        return listOf(pr1, pr2)
    }

    private fun stubGetRequestForPullRequests(pr1: GetPullRequest, linkHeader: String, pageNumber: Int) {
        stubFor(get("$PULL_REQUEST_PATH?page=$pageNumber").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Link", linkHeader)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(Klaxon().toJsonString((arrayOf(githubResponseFor(pr1)))))))
    }

    private fun githubResponseFor(getPullRequest: GetPullRequest): GetPullRequestResponse {
        return GetPullRequestResponse(getPullRequest.number, getPullRequest.title)
    }

}

private fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
    return this.withHeader("Accept", matching("application/json"))
            .withHeader("Authorization", matching("token .*"))
            .withHeader("Content-Type", matching("application/json"))
}

data class GetPullRequestResponse(val number: Int, val title: String)
