package it.pullrequestfactory.io

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.io.GithubHttpRepo
import java.io.File

class GithubHttpRepoTest {

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    @Test
    fun get_all_branches_for_given_repository_name() {
        val sut = GithubHttpRepo(baseUrl(), "repository-name", "basic-auth-token", branchesMustBeCached = false)

        stubForGithubBranchesRequestPage("repository-name")

        val branches = sut.get_all_branches()

        assertThat(branches).isNotEmpty
    }

    private fun baseUrl(): String {
        val resource = File("src/test/resources/app.properties")
        val properties = resource.readText()
        return properties.split("=")[1]
    }

    private fun stubForGithubBranchesRequestPage(repoName: String) {
        stubFor(get("/repos/ClausPolanka/$repoName/branches?page=1").willReturn(aResponse()
                .withStatus(200)
                .withHeader("Content-Type", "application/json; charset=utf-8")
                .withBody(Klaxon().toJsonString((arrayOf(GithubResponse(
                        name = "first_name_iteration_1_claus",
                        commit = GithubCommit(
                                sha = "4861382d8bd73481b98f72706cb57dc493de592b",
                                url = "https://api.github.com/repos/ClausPolanka/wordcount/commits/4861382d8bd73481b98f72706cb57dc493de592b"),
                        protected = false)))))))
    }

}

data class GithubResponse(val name: String, val commit: GithubCommit, val protected: Boolean)
data class GithubCommit(val sha: String, val url: String)
