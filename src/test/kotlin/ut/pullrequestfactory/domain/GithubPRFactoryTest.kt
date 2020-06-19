package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.*
import pullrequestfactory.io.ConsoleUI

private const val invalidBranchName = "firstname_lastname_claus"
private val candidate = Candidate("Firstname", "Lastname")
private val pairingpartner = listOf("Pairingpartner")

class GithubPRFactoryTest {

    @Test
    fun creates_two_pull_requests_for_different_sessions_and_different_iterations() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")), emptyList())
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs, mutableListOf()), ConsoleUI())

        sut.create_pull_requests(Candidate("Firstname", "Lastname"), listOf("Pairingpartner1", "Pairingpartner2"))

        assertThat(expectedPrs).containsExactly(
                PullRequest(
                        _title = "Firstname Lastname Iteration 1 / Session 1 Pairingpartner1 [PR]",
                        _base = Branch("master"),
                        _head = Branch("firstname_lastname_iteration_1_pairingpartner1")),
                PullRequest(
                        _title = "Firstname Lastname Iteration 2 / Session 2 Pairingpartner2",
                        _base = Branch("firstname_lastname_iteration_1_pairingpartner1"),
                        _head = Branch("firstname_lastname_iteration_2_pairingpartner2")))
    }

    @Test
    fun creates_pull_request_and_ignores_if_candidates_first_name_is_capitalized() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("firstname_lastname_iteration_1_pairingpartner")), emptyList())
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs, mutableListOf()), ConsoleUI())

        sut.create_pull_requests(Candidate(firstName = "Firstname", lastName = "lastname"), listOf("Pairingpartner"))

        assertThat(expectedPrs).containsExactly(
                PullRequest(
                        _title = "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                        _base = Branch("master"),
                        _head = Branch("firstname_lastname_iteration_1_pairingpartner")))
    }

    @Test
    fun creates_pull_request_and_ignores_if_candidates_last_name_is_capitalized() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("firstname_lastname_iteration_1_pairingpartner")), emptyList())
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs, mutableListOf()), ConsoleUI())

        sut.create_pull_requests(Candidate("firstname", "Lastname"), listOf("Pairingpartner"))

        assertThat(expectedPrs).containsExactly(
                PullRequest(
                        _title = "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                        _base = Branch("master"),
                        _head = Branch("firstname_lastname_iteration_1_pairingpartner")))
    }


    @Test
    fun creates_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_first_name() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("firstname1_lastname_iteration_1_pairingpartner")), emptyList())
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs, mutableListOf()), ConsoleUI())

        sut.create_pull_requests(Candidate("firstname2", "Lastname"), listOf("Pairingpartner"))

        assertThat(expectedPrs).isEmpty()
    }

    @Test
    fun creates_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_last_name() {
        val expectedPrs = mutableListOf<PullRequest>()
        val githubReadRepo = githubReadRepo(listOf(Branch("firstname_lastname1_iteration_1_pairingpartner")), emptyList())
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo(expectedPrs, mutableListOf()), ConsoleUI())

        sut.create_pull_requests(Candidate("Firstname", "lastname2"), listOf("Pairingpartner"))

        assertThat(expectedPrs).isEmpty()
    }

    @Test
    fun branch_can_not_be_processed_if_branch_name_has_invalid_name() {
        val sut = createGithubPRFactoryFor(invalidBranchName)

        assertThatThrownBy { sut.create_pull_requests(candidate, pairingpartner) }
                .hasMessageContaining("invalid name")
                .hasMessageContaining(invalidBranchName)
    }

    @Test
    fun close_pull_requests_for_two_candidates_with_same_first_name() {
        val pullRequestNumbersToBeClosed = mutableListOf<Int>()
        val sut = GithubPRFactory(
                githubReadRepo(emptyList(), listOf(
                        GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 pairingpartner"),
                        GetPullRequest(2, "Firstname1 Lastname2 Iteration 1 / Session 1 pairingpartner"))),
                githubWriteRepo(mutableListOf(), pullRequestNumbersToBeClosed),
                ConsoleUI())

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1)
    }

    @Test
    fun close_pull_requests_for_two_candidates_with_same_last_name() {
        val pullRequestNumbersToBeClosed = mutableListOf<Int>()
        val sut = GithubPRFactory(
                githubReadRepo(emptyList(), listOf(
                        GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 pairingpartner"),
                        GetPullRequest(2, "Firstname2 Lastname1 Iteration 1 / Session 1 pairingpartner"))),
                githubWriteRepo(mutableListOf(), pullRequestNumbersToBeClosed),
                ConsoleUI())

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1)
    }

    @Test
    fun close_pull_requests_for_one_candidate() {
        val pullRequestNumbersToBeClosed = mutableListOf<Int>()
        val sut = GithubPRFactory(
                githubReadRepo(emptyList(), listOf(
                        GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 pairingpartner1"),
                        GetPullRequest(2, "Firstname1 Lastname1 Iteration 1 / Session 2 pairingpartner2"))),
                githubWriteRepo(mutableListOf(), pullRequestNumbersToBeClosed),
                ConsoleUI())

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1, 2)
    }

    private fun githubReadRepo(branches: List<Branch>, pullRequests: List<GetPullRequest>): GithubReadRepo {
        return object : GithubReadRepo {
            override fun get_all_branches(): List<Branch> {
                return branches
            }

            override fun get_all_open_pull_requests(): List<GetPullRequest> {
                return pullRequests
            }
        }
    }

    private fun githubWriteRepo(expectedPrs: MutableList<PullRequest>, expectedPullRequestNumbersToBeClosed: MutableList<Int>): GithubWriteRepo {
        return object : GithubWriteRepo {
            override fun create_pull_request(pullRequest: PullRequest) {
                expectedPrs.add(pullRequest)
            }

            override fun close_pull_request(number: Int) {
                expectedPullRequestNumbersToBeClosed.add(number)
            }
        }
    }

    private fun noopGithubWriteRepo() = object : GithubWriteRepo {
        override fun create_pull_request(pullRequest: PullRequest) {
            // can be ignored in this test
        }

        override fun close_pull_request(number: Int) {
            // can be ignored in this test
        }
    }

    private fun createGithubPRFactoryFor(branchName: String) =
            GithubPRFactory(githubReadRepo(listOf(Branch(branchName)), emptyList()), noopGithubWriteRepo(), ConsoleUI())

}
