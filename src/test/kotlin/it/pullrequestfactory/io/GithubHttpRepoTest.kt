package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pullrequestfactory.io.GithubHttpRepo

class GithubHttpRepoTest {

    @Test
    fun finds_branches_for_given_candidate() {
        val sut = GithubHttpRepo(repoName = "wordcount")
        val branches = sut.get_all_branches()
        assertThat(branches).isNotEmpty
    }

}
