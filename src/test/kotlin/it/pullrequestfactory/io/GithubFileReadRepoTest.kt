package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.GithubFileReadRepo

class GithubFileReadRepoTest {

    @Test
    fun get_all_branches_from_files() {
        val sut = GithubFileReadRepo()

        val branches = sut.get_all_branches()

        assertThat(branches).hasSize(242)
    }

}
