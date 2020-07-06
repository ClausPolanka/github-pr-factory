package it.pullrequestfactory.io

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.domain.QuietUI
import pullrequestfactory.io.GithubHttpWriteRepo

class GithubHttpWriteRepoTest {

    private val REPO_PATH = "/repos/ClausPolanka/repository-name"
    private val WIRE_MOCK_DEFAULT_URL = "http://localhost:8080"
    private val PULL_REQUEST_PATH = "$REPO_PATH/pulls"
    private val ANY_SHA = "4861382d8bd73481b98f72706cb57dc493de592b"
    private val ANY_URL = "https://api.github.com/repos/ClausPolanka/wordcount/commits/4861382d8bd73481b98f72706cb57dc493de592b"
    private val pullRequest = PullRequest(
            _title = "Radek Leifer Iteration 1 / Session 1 Claus",
            _base = Branch("master"),
            _head = Branch("radek_leifer_interation_1_claus"))

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    @After
    fun tearDown() {
        reset()
    }

    @Test
    fun create_pull_request() {
        val sut = createGithubHttpRepo()

        sut.create_pull_request(pullRequest)

        verify(postRequestedFor(urlMatching(PULL_REQUEST_PATH))
                .withRequestBody(matching(jsonFor(pullRequest)))
                .addCommonHeaders())
    }

    @Test
    fun close_pull_request_for_given_pull_request_number() {
        val sut = createGithubHttpRepo()

        sut.close_pull_request(number = 1)

        verify(patchRequestedFor(urlMatching("$PULL_REQUEST_PATH/1"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .addCommonHeaders())
    }

    private fun createGithubHttpRepo(): GithubHttpWriteRepo = GithubHttpWriteRepo(
            WIRE_MOCK_DEFAULT_URL + REPO_PATH,
            "basic-auth-token",
            QuietUI())

    private fun jsonFor(pr: PullRequest) =
            Regex.escape("""{"base" : "${pr.base}", "head" : "${pr.head}", "title" : "${pr.title}"}""")

}

private fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
    return this
            .withHeader("Accept", matching("application/json"))
            .withHeader("Authorization", matching("Basic .*"))
            .withHeader("Content-Type", matching("application/json"))
}
