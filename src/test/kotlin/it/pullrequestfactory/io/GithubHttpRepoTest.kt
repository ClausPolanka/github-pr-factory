package it.pullrequestfactory.io

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.NoopCache
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.io.GithubHttpRepo
import java.io.File


class GithubHttpRepoTest {

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    private val repoName = "repository-name"

    @After
    fun tearDown() {
        WireMock.reset()
    }

    @Test
    fun get_all_branches_for_given_repository_name() {
        val branch = Branch("first_name_iteration_1_claus")
        val sut = createGithubHttpRepo()

        stubGithubRequestToReturn(branch.name)

        val branches = sut.get_all_branches()

        assertThat(branches).containsExactly(branch)
    }

    @Test
    fun create_pull_request() {
        val sut = createGithubHttpRepo()
        val pr = PullRequest(
                title = "Radek Leifer Iteration 1 / Session 1 Claus",
                base = "master",
                head = "radek_leifer_interation_1_claus")

        sut.create_pull_request(pr)

        verify(postRequestedFor(urlMatching("/repos/ClausPolanka/$repoName/pulls"))
                .withRequestBody(matching(Regex.escape(Klaxon().toJsonString(pr))))
                .withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("Basic .*"))
                .withHeader("Content-Type", matching("application/json")))

        val unmatched = findUnmatchedRequests()
        println(unmatched)
    }

    private fun createGithubHttpRepo() = GithubHttpRepo(baseUrl(), repoName, "basic-auth-token", NoopCache())

    private fun baseUrl(): String {
        val resource = File("src/test/resources/app.properties")
        val properties = resource.readText()
        return properties.split("=")[1]
    }

    private fun stubGithubRequestToReturn(branchName: String) {
        stubFor(get("/repos/ClausPolanka/$repoName/branches?page=1").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(Klaxon().toJsonString((arrayOf(GithubResponse(
                        name = branchName,
                        commit = GithubCommit(
                                sha = "4861382d8bd73481b98f72706cb57dc493de592b",
                                url = "https://api.github.com/repos/ClausPolanka/wordcount/commits/4861382d8bd73481b98f72706cb57dc493de592b"),
                        protected = false)))))))
    }

}

data class GithubResponse(val name: String, val commit: GithubCommit, val protected: Boolean)
data class GithubCommit(val sha: String, val url: String)
