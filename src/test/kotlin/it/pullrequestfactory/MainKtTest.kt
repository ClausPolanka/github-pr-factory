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
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.GetPullRequest
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.main
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream
import java.lang.System.lineSeparator
import java.nio.file.Files
import java.nio.file.Paths

private const val candidate = "Radek-Leifer"
private const val basicAuthToken = "any-valid-token"
private const val pairingPartner = "claus-berni-dominik-christian-shubi-markus-mihai"
private const val propsFileName = "app.properties"
private const val propsFilePath = "target/test-classes/$propsFileName"
private const val wireMockDefaultUrl = "http://localhost:8080"
private const val repoPath = "/repos/ClausPolanka/wordcount"
private const val linkHeader = "<https://api.github.com/repositories/157517927/branches?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/branches?page=9>; rel=\"last\""
private const val pullRequestPath = "/repos/ClausPolanka/wordcount/pulls"
private const val pullRequestLinkHeaderPage1 = "<https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"last\""
private const val pullRequestLinkHeaderPage2 = "<https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"prev\", <https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"first\""
private const val projectVersion = "1.0-SNAPSHOT"

class MainKtTest {

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
                "baseUrl=$wireMockDefaultUrl",
                "repoPath=$repoPath",
                "projectVersion=$projectVersion"))
        System.setOut(PrintStream(uiOutput))
    }

    @After
    fun tearDown() {
        reset()
        System.setIn(systemIn)
        System.setOut(systemOut)
        Files.deleteIfExists(Paths.get(propsFilePath))
    }

    @Test
    fun shows_version_output() {
        main(args = arrayOf("--version"))
        assertThat(uiOutput.toString()).contains("github-pr-factory version $projectVersion")
    }

    @Test
    fun shows_help_output() {
        main(args = arrayOf("--help"))
        assertThat(uiOutput.toString()).contains("Usage: github-pr-factory [OPTION] COMMAND")
    }

    @Test
    fun creates_pull_requests_for_the_given_program_arguments() {
        stubGetRequestsForGithubBranchesFromFiles()

        main(args = arrayOf("open", "-c", candidate, "-g", basicAuthToken, "-p", pairingPartner))

        verifyPostRequestsToGithubToCreatePullRequests(candidate)
    }

    @Test
    fun shows_open_command_usage_when_arguments_are_not_correct() {
        main(args = arrayOf("open", "-c", candidate, "-g", basicAuthToken, pairingPartner))

        assertThat(uiOutput.toString()).contains("Usage: github-pr-factory open [OPTION]")
    }

    @Test
    fun closes_pull_requests_for_given_program_arguments() {
        val prs = stubGetRequestForPullRequests(candidate)

        main(args = arrayOf("close", "-c", candidate, "-g", basicAuthToken))

        verifyPatchRequestToCloseOpenPullRequests(prs)
    }

    @Test
    fun shows_close_command_usage_when_arguments_are_not_correct() {
        main(args = arrayOf("close", "-c", candidate, "-g"))

        assertThat(uiOutput.toString()).contains("Usage: github-pr-factory close [OPTION]")
    }

    private fun verifyPatchRequestToCloseOpenPullRequests(prs: List<GetPullRequest>) {
        prs.forEach {
            verifyPatchRequestToCloseOpenPullRequestsFor(prNumber = it.number)
        }
    }

    private fun verifyPatchRequestToCloseOpenPullRequestsFor(prNumber: Int) {
        verify(patchRequestedFor(urlMatching("$pullRequestPath/$prNumber"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .addCommonHeaders())
    }

    private fun stubGetRequestsForGithubBranchesFromFiles() {
        (1..9).forEach {
            stubForGithubBranchesRequestPage(it)
        }
    }

    private fun stubForGithubBranchesRequestPage(pageNr: Int) {
        stubFor(get("/repos/ClausPolanka/wordcount/branches?page=$pageNr")
                .willReturn(aResponse().withStatus(200)
                        // Only important for first request
                        .withHeader("Link", linkHeader)
                        .withHeader("Content-Type", "application/json; charset=utf-8")
                        .withBody(File("json/branches-page-$pageNr.json").readText())))
    }

    private fun verifyPostRequestsToGithubToCreatePullRequests(candidate: String) {
        verify(8, postRequestedFor(urlEqualTo(pullRequestPath)))

        val candidateFirstName = candidate.split("-")[0]
        val candidateLastName = candidate.split("-")[1]

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 1 / Session 1 Claus",
                _base = Branch("master"),
                _head = Branch("radek_leifer_interation_1_claus")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 1 / Session 2 Berni",
                _base = Branch("radek_leifer_interation_1_claus"),
                _head = Branch("radek_leifer_interation_1_berni")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 1 / Session 3 Dominik [PR]",
                _base = Branch("radek_leifer_interation_1_berni"),
                _head = Branch("radek_leifer_iteration_1_dominik")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 2 / Session 3 Dominik",
                _base = Branch("radek_leifer_iteration_1_dominik"),
                _head = Branch("radek_leifer_iteration_2_dominik")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 2 / Session 4 Christian [PR]",
                _base = Branch("radek_leifer_iteration_2_dominik"),
                _head = Branch("radek_leifer_iteration_2_christian")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 3 / Session 5 Shubi",
                _base = Branch("radek_leifer_iteration_2_christian"),
                _head = Branch("radek_leifer_iteration_3_shubi")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 3 / Session 6 Markus",
                _base = Branch("radek_leifer_iteration_3_shubi"),
                _head = Branch("radek_leifer_iteration_3_markus")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "$candidateFirstName $candidateLastName Iteration 3 / Session 7 Mihai",
                _base = Branch("radek_leifer_iteration_3_markus"),
                _head = Branch("radek_leifer_iteration_3_mihai")))
    }

    private fun verifyPostRequestToGithubToCreatePullRequestFor(pr: PullRequest) {
        verify(postRequestedFor(urlMatching(pullRequestPath))
                .withRequestBody(matching(Regex.escape(Klaxon().toJsonString(pr))))
                .addCommonHeaders())
    }

    private fun createPropertyFileWith(props: List<String>) {
        Files.write(Paths.get(propsFilePath), props.joinToString(lineSeparator()).toByteArray())
    }

    private fun stubGetRequestForPullRequests(candidate: String): List<GetPullRequest> {
        val candidateFirstName = candidate.split("-")[0]
        val candidateLastName = candidate.split("-")[1]
        val pr1 = GetPullRequest(1, "$candidateFirstName $candidateLastName Iteration 1 / Session 1 Claus [PR]")
        val pr2 = GetPullRequest(2, "$candidateFirstName $candidateLastName Iteration 2 / Session 1 Claus")
        stubGetRequestForPullRequests(pr1, pullRequestLinkHeaderPage1, 1)
        stubGetRequestForPullRequests(pr2, pullRequestLinkHeaderPage2, 2)
        return listOf(pr1, pr2)
    }

    private fun stubGetRequestForPullRequests(pr1: GetPullRequest, linkHeader: String, pageNumber: Int) {
        stubFor(get("$pullRequestPath?page=$pageNumber").willReturn(aResponse()
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
            .withHeader("Authorization", matching("Basic .*"))
            .withHeader("Content-Type", matching("application/json"))
}

data class GetPullRequestResponse(val number: Int, val title: String)
