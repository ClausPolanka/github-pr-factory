package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.GithubHttpRepo

class GithubHttpRepoTest {

    @Test
    fun finds_branches_for_given_candidate() {
        val sut = GithubHttpRepo(repoName = "wordcount")
        val branches = sut.find_branches_for_candidate(Candidate("firstname", "lastname"))
        assertThat(branches).isEmpty()
    }

}
