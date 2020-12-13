package it.pullrequestfactory.io.repositories

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.QuietUI
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo

class GithubHttpWriteRepoTest {

    private val REPO_PATH = "/repos/ClausPolanka/repository-name"
    private val WIRE_MOCK_DEFAULT_URL = "http://localhost:8080"
    private val PULL_REQUEST_PATH = "$REPO_PATH/pulls"
    private val pullRequest = PullRequest(
            title = "Radek Leifer Iteration 1 / Session 1 Claus",
            _base = Branch("master"),
            _head = Branch("radek_leifer_interation_1_claus"))

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    @After
    fun tearDown() {
        WireMock.reset()
    }

    @Test
    fun create_pull_request() {
        val sut = createGithubHttpRepo()

        sut.open_pull_request(pullRequest)

        WireMock.verify(WireMock.postRequestedFor(WireMock.urlMatching(PULL_REQUEST_PATH))
                .withRequestBody(WireMock.matching(jsonFor(pullRequest)))
                .addCommonHeaders())
    }

    @Test
    fun close_pull_request_for_given_pull_request_number() {
        val sut = createGithubHttpRepo()

        sut.close_pull_request(number = 1)

        WireMock.verify(WireMock.patchRequestedFor(WireMock.urlMatching("$PULL_REQUEST_PATH/1"))
                .withRequestBody(WireMock.matching(Regex.escape("""{"state" : "closed"}""")))
                .addCommonHeaders())
    }

    private fun createGithubHttpRepo(): GithubHttpPullRequestsRepo = GithubHttpPullRequestsRepo(
            WIRE_MOCK_DEFAULT_URL + REPO_PATH,
            "auth-token",
            QuietUI())

    private fun jsonFor(pr: PullRequest) =
            Regex.escape("""{"base" : "${pr.base}", "head" : "${pr.head}", "title" : "${pr.title}"}""")

    private fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
        return this
                .withHeader("Accept", WireMock.matching("application/json"))
                .withHeader("Authorization", WireMock.matching("token.*"))
                .withHeader("Content-Type", WireMock.matching("application/json"))
    }
}
