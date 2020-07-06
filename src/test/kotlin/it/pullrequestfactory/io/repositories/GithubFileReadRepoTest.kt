package it.pullrequestfactory.io.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.repositories.GithubFileBranchesRepo

class GithubFileReadRepoTest {

    @Test
    fun get_all_branches_from_files() {
        val sut = GithubFileBranchesRepo()

        val branches = sut.get_all_branches()

        assertThat(branches).hasSize(242)
    }

    @Test
    fun get_all_open_pull_requests_from_files() {
        val sut = GithubFileBranchesRepo()

        val prs = sut.get_all_open_pull_requests()

        assertThat(prs).hasSize(60)
    }

}
