package it.pullrequestfactory.io.repositories

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.QuietUI
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import kotlin.text.Regex.Companion.escape

class GithubHttpWriteRepoTest {

    private val repoPath = "/repos/ClausPolanka/repository-name"
    private val wireMockDefaultUrl = "http://localhost:8080"
    private val pullRequestPath = "$repoPath/pulls"
    private val pullRequest = PullRequest(
        title = "Radek Leifer Iteration 1 / Session 1 Claus",
        _base = Branch("master"),
        _head = Branch("radek_leifer_interation_1_claus")
    )

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
    fun `open pull request`() {
        val sut = createGithubHttpRepo()

        sut.openPullRequest(pullRequest)

        verify(
            postRequestedFor(urlMatching(pullRequestPath))
                .withRequestBody(matching(jsonFor(pullRequest)))
                .addCommonHeaders()
        )
    }

    @Test
    fun `close pull request for given pull request number`() {
        val sut = createGithubHttpRepo()

        sut.closePullRequest(number = 1)

        verify(
            patchRequestedFor(urlMatching("$pullRequestPath/1"))
                .withRequestBody(matching(escape("""{"state" : "closed"}""")))
                .addCommonHeaders()
        )
    }

    private fun createGithubHttpRepo(): GithubHttpPullRequestsRepo = GithubHttpPullRequestsRepo(
        wireMockDefaultUrl + repoPath,
        KhttpClientStats(KhttpClient("auth-token")),
        QuietUI()
    )

    private fun jsonFor(pr: PullRequest) =
        escape("""{"base" : "${pr.base}", "head" : "${pr.head}", "title" : "${pr.title}"}""")

    private fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
        return this
            .withHeader("Accept", matching("application/json"))
            .withHeader("Authorization", matching("token.*"))
            .withHeader("Content-Type", matching("application/json"))
    }
}
