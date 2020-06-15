package it.pullrequestfactory.io

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.NoopCache
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.domain.QuietUI
import pullrequestfactory.io.GithubHttpRepo

class GithubHttpRepoTest {

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    private val linkHeader = "<https://api.github.com/repositories/157517927/branches?page=2>; rel=\"next\", " +
            "<https://api.github.com/repositories/157517927/branches?page=2>; rel=\"last\""
    private val repoName = "repository-name"
    private val wireMockDefaultUrl = "http://localhost:8080"

    @After
    fun tearDown() {
        reset()
    }

    @Test
    fun get_all_branches_for_github_repository_which_contains_only_one_branch() {
        val branch = Branch("first_name_iteration_1_claus")
        val sut = createGithubHttpRepo()

        stubGithubGetRequestToReturn(branch)

        val branches = sut.get_all_branches()

        assertThat(branches).containsExactly(branch)
    }

    @Test
    fun get_all_branches_for_github_repository_which_contains_two_pages_of_branches() {
        val branch1 = Branch("first_name_iteration_1_claus")
        val branch2 = Branch("first_name_iteration_2_claus")
        val sut = createGithubHttpRepo()

        stubGithubGetRequstForPageOneToReturn(branch1)
        stubGithubRequestForPageTwoContaining(branch2)

        val branches = sut.get_all_branches()

        assertThat(branches).containsExactly(branch1, branch2)
    }

    @Test
    fun branches_are_empty_when_Github_returs_403_forbidden_status_code() {
        val sut = createGithubHttpRepo()

        stubFor(get("/repos/ClausPolanka/$repoName/branches?page=1")
                .willReturn(aResponse().withStatus(403)))

        val branches = sut.get_all_branches()

        assertThat(branches).isEmpty()
    }

    @Test
    fun create_pull_request() {
        val sut = createGithubHttpRepo()
        val pr = PullRequest(
                _title = "Radek Leifer Iteration 1 / Session 1 Claus",
                _base = Branch("master"),
                _head = Branch("radek_leifer_interation_1_claus"))

        sut.create_pull_request(pr)

        verify(postRequestedFor(urlMatching("/repos/ClausPolanka/$repoName/pulls"))
                .withRequestBody(matching(Regex.escape("""{"base" : "master", "head" : "radek_leifer_interation_1_claus", "title" : "Radek Leifer Iteration 1 / Session 1 Claus"}""")))
                .withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("Basic .*"))
                .withHeader("Content-Type", matching("application/json")))
    }

    @Test
    fun close_pull_request_for_given_pull_request_number() {
        val sut = createGithubHttpRepo()

        sut.close_pull_request(number = 1)

        verify(patchRequestedFor(urlMatching("/repos/ClausPolanka/$repoName/pulls/1"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("Basic .*"))
                .withHeader("Content-Type", matching("application/json")))
    }

    private fun createGithubHttpRepo(): GithubHttpRepo = GithubHttpRepo(
            wireMockDefaultUrl,
            repoName,
            "basic-auth-token",
            NoopCache(),
            QuietUI())

    private fun stubGithubGetRequestToReturn(branch: Branch) {
        stubFor(get("/repos/ClausPolanka/$repoName/branches?page=1").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(Klaxon().toJsonString((arrayOf(githubResponseFor(branch)))))))
    }

    private fun githubResponseFor(branch: Branch): GithubResponse {
        return GithubResponse(
                name = branch.name,
                commit = GithubCommit(
                        sha = "4861382d8bd73481b98f72706cb57dc493de592b",
                        url = "https://api.github.com/repos/ClausPolanka/wordcount/commits/4861382d8bd73481b98f72706cb57dc493de592b"),
                protected = false)
    }

    private fun stubGithubGetRequstForPageOneToReturn(branch: Branch) {
        stubFor(get("/repos/ClausPolanka/$repoName/branches?page=1").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withHeader("Link", linkHeader)
                .withBody(Klaxon().toJsonString((arrayOf(githubResponseFor(branch)))))))
    }

    private fun stubGithubRequestForPageTwoContaining(branch: Branch) {
        stubFor(get("/repos/ClausPolanka/$repoName/branches?page=2").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(Klaxon().toJsonString((arrayOf(githubResponseFor(branch)))))))
    }

}

data class GithubResponse(val name: String, val commit: GithubCommit, val protected: Boolean)
data class GithubCommit(val sha: String, val url: String)
