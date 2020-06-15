package it.pullrequestfactory

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
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
import java.nio.file.Files
import java.nio.file.Paths

class MainKtTest {

    private val systemIn = System.`in`
    private val systemOut = System.out
    private val userOutput = ByteArrayOutputStream()
    private val propsFileName = "app.properties"
    private val wireMockDefaultUrl = "http://localhost:8080"
    private val linkHeader = "<https://api.github.com/repositories/157517927/branches?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/branches?page=9>; rel=\"last\""

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    @Before
    fun setUp() {
        createPropsWith("baseUrl=$wireMockDefaultUrl")
        System.setOut(PrintStream(userOutput))
    }

    @After
    fun tearDown() {
        reset()
        System.setIn(systemIn)
        System.setOut(systemOut)
        Files.deleteIfExists(Paths.get("target/test-classes/$propsFileName"))
    }

    @Test
    fun no_pull_requets_created_for_wrong_number_of_arguments() {
        val args = emptyArray<String>()
        main(args)
        assertThat(userOutput.toString()).contains("Wrong number of arguments")
    }

    @Test
    fun create_pull_requests_for_the_given_candidate() {
        val candidate = "Radek-Leifer"
        val basicAuthToken = "any-valid-token"
        val pairingPartner = "claus-berni-dominik-christian-shubi-markus-mihai"

        stubRequestsForGithubBranches()

        main(args = arrayOf(candidate, basicAuthToken, pairingPartner))

        verifyPostRequestsToGithubToCreatePullRequests()
    }

    @Test
    fun close_pull_requests_for_given_candidate() {
        val candidate = "Radek-Leifer"
        val basicAuthToken = "any-valid-token"
        val closePullRequestOption = "-close"

        stubGetRequestForPullRequests()

        main(args = arrayOf(candidate, basicAuthToken, closePullRequestOption))

        verifyPatchRequestToCloseOpenPullRequestsForCandidate()
    }

    private fun verifyPatchRequestToCloseOpenPullRequestsForCandidate() {
        verify(patchRequestedFor(urlMatching("/repos/ClausPolanka/wordcount/pulls/1"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("Basic .*"))
                .withHeader("Content-Type", matching("application/json")))

        verify(patchRequestedFor(urlMatching("/repos/ClausPolanka/wordcount/pulls/2"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("Basic .*"))
                .withHeader("Content-Type", matching("application/json")))
    }

    private fun stubRequestsForGithubBranches() {
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

    private fun verifyPostRequestsToGithubToCreatePullRequests() {
        verify(8, postRequestedFor(urlEqualTo("/repos/ClausPolanka/wordcount/pulls")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 1 / Session 1 Claus",
                _base = Branch("master"),
                _head = Branch("radek_leifer_interation_1_claus")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 1 / Session 2 Berni",
                _base = Branch("radek_leifer_interation_1_claus"),
                _head = Branch("radek_leifer_interation_1_berni")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 1 / Session 3 Dominik [PR]",
                _base = Branch("radek_leifer_interation_1_berni"),
                _head = Branch("radek_leifer_iteration_1_dominik")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 2 / Session 3 Dominik",
                _base = Branch("radek_leifer_iteration_1_dominik"),
                _head = Branch("radek_leifer_iteration_2_dominik")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 2 / Session 4 Christian [PR]",
                _base = Branch("radek_leifer_iteration_2_dominik"),
                _head = Branch("radek_leifer_iteration_2_christian")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 3 / Session 5 Shubi",
                _base = Branch("radek_leifer_iteration_2_christian"),
                _head = Branch("radek_leifer_iteration_3_shubi")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 3 / Session 6 Markus",
                _base = Branch("radek_leifer_iteration_3_shubi"),
                _head = Branch("radek_leifer_iteration_3_markus")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                _title = "Radek Leifer Iteration 3 / Session 7 Mihai",
                _base = Branch("radek_leifer_iteration_3_markus"),
                _head = Branch("radek_leifer_iteration_3_mihai")))
    }

    private fun verifyPostRequestToGithubToCreatePullRequestFor(pr: PullRequest) {
        verify(postRequestedFor(urlMatching("/repos/ClausPolanka/wordcount/pulls"))
                .withRequestBody(matching(Regex.escape(Klaxon().toJsonString(pr))))
                .withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("Basic .*"))
                .withHeader("Content-Type", matching("application/json")))
    }

    private fun createPropsWith(prop: String): String {
        Files.write(Paths.get("target/test-classes/$propsFileName"), prop.toByteArray())
        return propsFileName
    }

    private fun stubGetRequestForPullRequests() {
        stubFor(get("/repos/ClausPolanka/wordcount/pulls?page=1").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Link", "<https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"last\"")
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(Klaxon().toJsonString((arrayOf(
                        githubResponseFor(GetPullRequest(1, "Radek Leifer Iteration 1 / Session 1 Claus [PR]"))))))))

        stubFor(get("/repos/ClausPolanka/wordcount/pulls?page=2").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Link", "<https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"prev\", <https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"first\"")
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(Klaxon().toJsonString((arrayOf(
                        githubResponseFor(GetPullRequest(2, "Radek Leifer Iteration 2 / Session 1 Claus"))))))))
    }

    private fun githubResponseFor(getPullRequest: GetPullRequest): GetPullRequestResponse {
        return GetPullRequestResponse(getPullRequest.number, getPullRequest.title)
    }

}

data class GetPullRequestResponse(val number: Int, val title: String)
