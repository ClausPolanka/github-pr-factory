package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.*
import pullrequestfactory.domain.uis.QuietUI

class GithubPRFactoryTest {

    private val INVALID_BRANCH_NAME = "firstname_lastname_claus"
    private val candidate = Candidate("Firstname", "Lastname")
    private val pairingpartner = listOf("Pairingpartner")

    @Test
    fun opens_two_pull_requests_for_different_iterations_and_pairing_partner() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")))

        sut.open_pull_requests(Candidate("Firstname", "Lastname"), listOf("Pairingpartner1", "Pairingpartner2"))

        assertThat(pullRequests).containsExactly(
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
    fun opens_pull_request_and_ignores_if_candidates_first_name_is_capitalized() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner")))

        sut.open_pull_requests(Candidate(firstName = "Firstname", lastName = "lastname"), listOf("Pairingpartner"))

        assertThat(pullRequests).containsExactly(PullRequest(
                _title = "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                _base = Branch("master"),
                _head = Branch("firstname_lastname_iteration_1_pairingpartner")))
    }

    @Test
    fun opens_pull_request_and_ignores_if_candidates_last_name_is_capitalized() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner")))

        sut.open_pull_requests(Candidate("firstname", "Lastname"), listOf("Pairingpartner"))

        assertThat(pullRequests).containsExactly(PullRequest(
                _title = "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                _base = Branch("master"),
                _head = Branch("firstname_lastname_iteration_1_pairingpartner")))
    }

    @Test
    fun opens_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_first_name() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname1_lastname_iteration_1_pairingpartner")))

        sut.open_pull_requests(Candidate("firstname2", "Lastname"), listOf("Pairingpartner"))

        assertThat(pullRequests).isEmpty()
    }

    @Test
    fun opens_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_last_name() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname1_iteration_1_pairingpartner")))

        sut.open_pull_requests(Candidate("Firstname", "lastname2"), listOf("Pairingpartner"))

        assertThat(pullRequests).isEmpty()
    }

    @Test
    fun branch_can_not_be_processed_if_branch_name_has_invalid_name() {
        val sut = create_github_pr_factory_for(INVALID_BRANCH_NAME)

        assertThatThrownBy { sut.open_pull_requests(candidate, pairingpartner) }
                .hasMessageContaining("invalid name")
                .hasMessageContaining(INVALID_BRANCH_NAME)
    }

    @Test
    fun close_pull_requests_for_two_candidates_with_same_first_name() {
        val (pullRequestNumbersToBeClosed, sut) = create_github_pr_factory_for(listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 pairingpartner"),
                GetPullRequest(2, "Firstname1 Lastname2 Iteration 1 / Session 1 pairingpartner")))

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1)
    }

    @Test
    fun close_pull_requests_for_two_candidates_with_same_last_name() {
        val (pullRequestNumbersToBeClosed, sut) = create_github_pr_factory_for(listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 pairingpartner"),
                GetPullRequest(2, "Firstname2 Lastname1 Iteration 1 / Session 1 pairingpartner")))

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1)
    }

    @Test
    fun close_pull_requests_for_one_candidate() {
        val (pullRequestNumbersToBeClosed, sut) = create_github_pr_factory_for(listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 pairingpartner1"),
                GetPullRequest(2, "Firstname1 Lastname1 Iteration 1 / Session 2 pairingpartner2")))

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1, 2)
    }

    private fun create_github_pr_factory(branches: List<Branch>): Pair<MutableList<PullRequest>, GithubPRFactory> {
        val pullRequests = mutableListOf<PullRequest>()
        val githubBranchesRepo = github_branches_repo(branches)
        val sut = GithubPRFactory(
                githubBranchesRepo,
                github_pull_requests_repo(github_write_repo(pullRequests)),
                BranchSyntaxValidator(QuietUI()),
                PullRequests(PullRequestLastNotFinishedMarker()))
        return Pair(pullRequests, sut)
    }

    private fun create_github_pr_factory_for(pullRequests: List<GetPullRequest>): Pair<MutableList<Int>, GithubPRFactory> {
        val pullRequestNumbersToBeClosed = mutableListOf<Int>()
        val sut = GithubPRFactory(
                github_branches_repo(emptyList()),
                github_pull_requests_repo(
                        github_write_repo(mutableListOf(), pullRequestNumbersToBeClosed),
                        pullRequests.toMutableList()),
                BranchSyntaxValidator(QuietUI()),
                PullRequests(PullRequestLastNotFinishedMarker()))
        return Pair(pullRequestNumbersToBeClosed, sut)
    }

    private fun create_github_pr_factory_for(branchName: String) =
            GithubPRFactory(
                    github_branches_repo(listOf(Branch(branchName))),
                    github_pull_requests_repo(noop_github_write_repo()),
                    BranchSyntaxValidator(QuietUI()),
                    PullRequests(PullRequestLastNotFinishedMarker()))

    private fun github_branches_repo(branches: List<Branch>): GithubBranchesRepo {
        return object : GithubBranchesRepo {
            override fun get_all_branches(): List<Branch> {
                return branches
            }
        }
    }

    private fun github_pull_requests_repo(writeRepo: GithubPullRequestsWriteRepo, expectedPrs: MutableList<GetPullRequest> = mutableListOf()): GithubPullRequestsRepo {
        return object : GithubPullRequestsRepo {
            override fun get_all_open_pull_requests(): List<GetPullRequest> {
                return expectedPrs
            }

            override fun open_pull_request(pullRequest: PullRequest) {
                writeRepo.open_pull_request(pullRequest)
            }

            override fun close_pull_request(number: Int) {
                writeRepo.close_pull_request(number)
            }
        }
    }

    private fun github_pull_requests_read_repo(pullRequests: List<GetPullRequest>): GithubPullRequestsReadRepo {
        return object : GithubPullRequestsReadRepo {
            override fun get_all_open_pull_requests(): List<GetPullRequest> {
                return pullRequests
            }
        }
    }

    private fun github_write_repo(expectedPrs: MutableList<PullRequest>, expectedPullRequestNumbersToBeClosed: MutableList<Int> = mutableListOf()): GithubPullRequestsWriteRepo {
        return object : GithubPullRequestsWriteRepo {
            override fun open_pull_request(pullRequest: PullRequest) {
                expectedPrs.add(pullRequest)
            }

            override fun close_pull_request(number: Int) {
                expectedPullRequestNumbersToBeClosed.add(number)
            }
        }
    }

    private fun noop_github_write_repo() = object : GithubPullRequestsWriteRepo {
        override fun open_pull_request(pullRequest: PullRequest) {
            // can be ignored in this test
        }

        override fun close_pull_request(number: Int) {
            // can be ignored in this test
        }
    }

}
