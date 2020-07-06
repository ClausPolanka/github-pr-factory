package it.pullrequestfactory.io

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import it.pullrequestfactory.GetPullRequestResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.*
import pullrequestfactory.io.GithubHttpRepo

class GithubHttpRepoTest {

    private val REPO_PATH = "/repos/ClausPolanka/repository-name"
    private val WIRE_MOCK_DEFAULT_URL = "http://localhost:8080"
    private val LINK_HEADER_FOR_PAGE_1 = "<https://api.github.com/repositories/157517927/branches?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/branches?page=2>; rel=\"last\""
    private val LINK_HEADER_PULL_REQUESTS_PAGE_1 = "<https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"next\", <https://api.github.com/repositories/157517927/pulls?page=2>; rel=\"last\""
    private val LINK_HEADER_PULL_REQUESTS_PAGE_2 = "<https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"prev\", <https://api.github.com/repositories/157517927/pulls?page=1>; rel=\"first\""
    private val CONTENT_TYPE_HEADER = "application/json; charset=utf-8"
    private val PULL_REQUEST_PATH = "$REPO_PATH/pulls"
    private val PULL_REQUEST_PATH_PAGE_1 = "$PULL_REQUEST_PATH?page=1"
    private val BRANCH_REQUEST_PATH_PAGE_1 = "$REPO_PATH/branches?page=1"
    private val BRANCH_REQUEST_PATH_PAGE_2 = "$REPO_PATH/branches?page=2"
    private val ANY_SHA = "4861382d8bd73481b98f72706cb57dc493de592b"
    private val ANY_URL = "https://api.github.com/repos/ClausPolanka/wordcount/commits/4861382d8bd73481b98f72706cb57dc493de592b"
    private val branch = Branch("first_name_iteration_1_claus")
    private val branch1 = Branch("first_name_iteration_1_claus")
    private val branch2 = Branch("first_name_iteration_2_claus")
    private val pullRequest = PullRequest(
            _title = "Radek Leifer Iteration 1 / Session 1 Claus",
            _base = Branch("master"),
            _head = Branch("radek_leifer_interation_1_claus"))
    private val getPullRequest = GetPullRequest(1, "firstname lastname Iteration 1 / Session 1 pairingpartner")
    private val getPullRequest1 = GetPullRequest(1, "firstname lastname Iteration 1 / Session 1 pairingpartner1 [PR]")
    private val getPullRequest2 = GetPullRequest(1, "firstname lastname Iteration 2 / Session 1 pairingpartner1")

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
    fun get_all_branches_for_github_repository_which_contains_only_one_branch() {
        val sut = createGithubHttpRepo()

        stubGithubGetRequestToReturn(branch)

        val branches = sut.get_all_branches()

        assertThat(branches).containsExactly(branch)
    }

    @Test
    fun get_all_branches_for_github_repository_which_contains_two_pages_of_branches() {
        val sut = createGithubHttpRepo()

        stubGithubGetRequestForPageOneToReturn(branch1)
        stubGithubRequestForPageTwoContaining(branch2)

        val branches = sut.get_all_branches()

        assertThat(branches).containsExactly(branch1, branch2)
    }

    @Test
    fun branches_are_empty_when_Github_returs_403_forbidden_status_code() {
        val sut = createGithubHttpRepo()

        stubFor(get(BRANCH_REQUEST_PATH_PAGE_1).willReturn(aResponse().withStatus(403)))

        val branches = sut.get_all_branches()

        assertThat(branches).isEmpty()
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
    fun get_all_open_pull_requests_for_github_repository_which_contains_one_page_of_pull_requests() {
        val sut = createGithubHttpRepo()

        stubGithubGetRequestToReturn(getPullRequest)

        val prs = sut.get_all_open_pull_requests()

        assertThat(prs).containsExactly(getPullRequest)
    }

    @Test
    fun get_all_open_pull_requests_for_github_repository_which_contains_two_pages_of_pull_requests() {
        val sut = createGithubHttpRepo()

        stubGithubGetRequestForPageOneToReturn(getPullRequest1)
        stubGithubGetRequestForPageTwoToReturn(getPullRequest2)

        val prs = sut.get_all_open_pull_requests()

        assertThat(prs).containsExactly(getPullRequest1, getPullRequest2)
    }

    @Test
    fun pull_requests_are_empty_when_Github_returs_403_forbidden_status_code() {
        val sut = createGithubHttpRepo()

        stubFor(get(PULL_REQUEST_PATH_PAGE_1).willReturn(aResponse().withStatus(403)))

        val prs = sut.get_all_open_pull_requests()

        assertThat(prs).isEmpty()
    }

    @Test
    fun close_pull_request_for_given_pull_request_number() {
        val sut = createGithubHttpRepo()

        sut.close_pull_request(number = 1)

        verify(patchRequestedFor(urlMatching("$PULL_REQUEST_PATH/1"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .addCommonHeaders())
    }

    private fun createGithubHttpRepo(): GithubHttpRepo = GithubHttpRepo(
            WIRE_MOCK_DEFAULT_URL,
            REPO_PATH,
            "basic-auth-token",
            NoopCache(),
            QuietUI())

    private fun stubGithubGetRequestToReturn(branch: Branch) {
        stubFor(get(BRANCH_REQUEST_PATH_PAGE_1).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                .withBody(Klaxon().toJsonString((arrayOf(githubResponseFor(branch)))))))
    }

    private fun githubResponseFor(branch: Branch): GithubResponse {
        return GithubResponse(
                name = branch.name,
                commit = GithubCommit(ANY_SHA, ANY_URL),
                protected = false)
    }

    private fun stubGithubGetRequestForPageOneToReturn(branch: Branch) {
        stubFor(get(BRANCH_REQUEST_PATH_PAGE_1).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                .withHeader("Link", LINK_HEADER_FOR_PAGE_1)
                .withBody(Klaxon().toJsonString((arrayOf(githubResponseFor(branch)))))))
    }

    private fun stubGithubRequestForPageTwoContaining(branch: Branch) {
        stubFor(get(BRANCH_REQUEST_PATH_PAGE_2).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                .withBody(Klaxon().toJsonString((arrayOf(githubResponseFor(branch)))))))
    }

    private fun githubResponseFor(getPullRequest: GetPullRequest): GetPullRequestResponse {
        return GetPullRequestResponse(getPullRequest.number, getPullRequest.title)
    }

    private fun stubGithubGetRequestToReturn(expectedGetPullRequest: GetPullRequest) {
        stubFor(get(PULL_REQUEST_PATH_PAGE_1).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                .withBody(Klaxon().toJsonString((arrayOf(
                        githubResponseFor(expectedGetPullRequest)))))))
    }

    private fun stubGithubGetRequestForPageOneToReturn(expectedGetPullRequest: GetPullRequest) {
        stubFor(get(PULL_REQUEST_PATH_PAGE_1).willReturn(aResponse()
                .withStatus(200)
                .withHeader("Link", LINK_HEADER_PULL_REQUESTS_PAGE_1)
                .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                .withBody(Klaxon().toJsonString((arrayOf(
                        githubResponseFor(expectedGetPullRequest)))))))
    }

    private fun stubGithubGetRequestForPageTwoToReturn(expectedGetPullRequest: GetPullRequest) {
        stubFor(get("$PULL_REQUEST_PATH?page=2").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Link", LINK_HEADER_PULL_REQUESTS_PAGE_2)
                .withHeader("Content-Type", CONTENT_TYPE_HEADER)
                .withBody(Klaxon().toJsonString((arrayOf(
                        githubResponseFor(expectedGetPullRequest)))))))
    }

    private fun jsonFor(pr: PullRequest) =
            Regex.escape("""{"base" : "${pr.base}", "head" : "${pr.head}", "title" : "${pr.title}"}""")

}

private fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
    return this
            .withHeader("Accept", matching("application/json"))
            .withHeader("Authorization", matching("Basic .*"))
            .withHeader("Content-Type", matching("application/json"))
}

data class GithubResponse(val name: String, val commit: GithubCommit, val protected: Boolean)
data class GithubCommit(val sha: String, val url: String)
