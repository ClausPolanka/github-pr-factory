package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.GithubHttpRepo

class GithubHttpRepoTest {

    @Test
    fun get_all_branches_for_given_repository_name() {
        val sut = GithubHttpRepo(repoName = "wordcount", basicAuthToken = "ignore")
        val branches = sut.get_all_branches()
        assertThat(branches).isNotEmpty
    }

}
