package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions
import org.junit.Test
import pullrequestfactory.domain.*


class GithubPRFactoryTest {

    @Test
    fun create_two_pull_requests_for_different_sessions_and_different_iterations() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(
                Branch("firstname_lastname_iteration_1_claus"),
                Branch("firstname_lastname_iteration_2_berni")))
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs))

        sut.create_pull_requests(Candidate("Firstname", "Lastname"), listOf("Claus", "Berni"))

        Assertions.assertThat(expectedPrs).containsExactly(
                PullRequest(
                        title = "Firstname Lastname Iteration 1 / Session 1 Claus [PR]",
                        base = "master",
                        head = "firstname_lastname_iteration_1_claus"),
                PullRequest(
                        title = "Firstname Lastname Iteration 2 / Session 2 Berni",
                        base = "firstname_lastname_iteration_1_claus",
                        head = "firstname_lastname_iteration_2_berni"))
    }

    private fun githubReadRepo(branches: List<Branch>): GithubReadRepo {
        return object : GithubReadRepo {
            override fun get_all_branches(): List<Branch> {
                return branches
            }
        }
    }

    private fun githubWriteRepo(expectedPrs: MutableList<PullRequest>): GithubWriteRepo {
        return object : GithubWriteRepo {
            override fun create_pull_request(pullRequest: PullRequest) {
                expectedPrs.add(pullRequest)
            }
        }
    }
}
