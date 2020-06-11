package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.*
import pullrequestfactory.io.ConsoleUI


class GithubPRFactoryTest {

    @Test
    fun creates_two_pull_requests_for_different_sessions_and_different_iterations() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(
                Branch("firstname_lastname_iteration_1_claus"),
                Branch("firstname_lastname_iteration_2_berni")))
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs), ConsoleUI())

        sut.create_pull_requests(Candidate("Firstname", "Lastname"), listOf("Claus", "Berni"))

        assertThat(expectedPrs).containsExactly(
                PullRequest(
                        _title = "Firstname Lastname Iteration 1 / Session 1 Claus [PR]",
                        base = "master",
                        head = "firstname_lastname_iteration_1_claus"),
                PullRequest(
                        _title = "Firstname Lastname Iteration 2 / Session 2 Berni",
                        base = "firstname_lastname_iteration_1_claus",
                        head = "firstname_lastname_iteration_2_berni"))
    }

    @Test
    fun creates_pull_request_and_ignores_if_candidates_first_name_is_capitalized() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("a_lastname_iteration_1_claus")))
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs), ConsoleUI())

        sut.create_pull_requests(Candidate("A", "lastname"), listOf("Claus"))

        assertThat(expectedPrs).containsExactly(
                PullRequest(
                        _title = "A Lastname Iteration 1 / Session 1 Claus",
                        base = "master",
                        head = "a_lastname_iteration_1_claus"))
    }


    @Test
    fun creates_pull_request_and_ignores_if_candidates_last_name_is_capitalized() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("firstname_a_iteration_1_claus")))
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs), ConsoleUI())

        sut.create_pull_requests(Candidate("Firstname", "A"), listOf("Claus"))

        assertThat(expectedPrs).containsExactly(
                PullRequest(
                        _title = "Firstname A Iteration 1 / Session 1 Claus",
                        base = "master",
                        head = "firstname_a_iteration_1_claus"))
    }

    @Test
    fun creates_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_first_name() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("a_lastname_iteration_1_claus")))
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs), ConsoleUI())

        sut.create_pull_requests(Candidate("B", "Lastname"), listOf("Claus"))

        assertThat(expectedPrs).isEmpty()
    }

    @Test
    fun creates_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_last_name() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("firstname_a_iteration_1_claus")))
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs), ConsoleUI())

        sut.create_pull_requests(Candidate("Firstname", "b"), listOf("Claus"))

        assertThat(expectedPrs).isEmpty()
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
