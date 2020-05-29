package it.pullrequestfactory

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.main
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.PrintStream


class MainKtTest {

    private val systemIn = System.`in`
    private val systemOut = System.out
    private val userOutput = ByteArrayOutputStream()

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    @Before
    fun setUp() {
        System.setOut(PrintStream(userOutput))
    }

    @After
    fun tearDown() {
        WireMock.reset()
        System.setIn(systemIn)
        System.setOut(systemOut)
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

    private fun stubRequestsForGithubBranches() {
        (1..9).forEach {
            stubForGithubBranchesRequestPage(it)
        }
    }

    private fun stubForGithubBranchesRequestPage(pageNr: Int) {
        stubFor(get("/repos/ClausPolanka/wordcount/branches?page=$pageNr")
                .willReturn(
                        aResponse()
                                .withStatus(200)
                                // Only important for first request
                                .withHeader("Link", "<https://api.github.com/repositories/157517927/branches?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/branches?page=9>; rel=\"last\"")
                                .withHeader("Content-Type", "application/json; charset=utf-8")
                                .withBody(File("json/branches-page-$pageNr.json").readText()))
        )
    }

    private fun verifyPostRequestsToGithubToCreatePullRequests() {
        verify(8, postRequestedFor(urlEqualTo("/repos/ClausPolanka/wordcount/pulls")))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 1 / Session 1 Claus",
                base = "master",
                head = "radek_leifer_interation_1_claus"))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 1 / Session 2 Berni",
                base = "radek_leifer_interation_1_claus",
                head = "radek_leifer_interation_1_berni"))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 1 / Session 3 Dominik [PR]",
                base = "radek_leifer_interation_1_berni",
                head = "radek_leifer_iteration_1_dominik"
        ))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 2 / Session 3 Dominik",
                base = "radek_leifer_iteration_1_dominik",
                head = "radek_leifer_iteration_2_dominik"))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 2 / Session 4 Christian [PR]",
                base = "radek_leifer_iteration_2_dominik",
                head = "radek_leifer_iteration_2_christian"))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 3 / Session 5 Shubi",
                base = "radek_leifer_iteration_2_christian",
                head = "radek_leifer_iteration_3_shubi"))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 3 / Session 6 Markus",
                base = "radek_leifer_iteration_3_shubi",
                head = "radek_leifer_iteration_3_markus"
        ))

        verifyPostRequestToGithubToCreatePullRequestFor(PullRequest(
                title = "Radek Leifer Iteration 3 / Session 7 Mihai",
                base = "radek_leifer_iteration_3_markus",
                head = "radek_leifer_iteration_3_mihai"
        ))
    }

    private fun verifyPostRequestToGithubToCreatePullRequestFor(pr: PullRequest) {
        verify(postRequestedFor(urlMatching("/repos/ClausPolanka/wordcount/pulls"))
                .withRequestBody(matching(Regex.escape(Klaxon().toJsonString(pr))))
                .withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("Basic .*"))
                .withHeader("Content-Type", matching("application/json")))
    }


}
