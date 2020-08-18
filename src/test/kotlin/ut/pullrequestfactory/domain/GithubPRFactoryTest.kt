package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.*
import pullrequestfactory.domain.uis.QuietUI
import pullrequestfactory.io.uis.ConsoleUI

class GithubPRFactoryTest {

    private val INVALID_BRANCH_NAME = "firstname_lastname_claus"
    private val candidate = Candidate("Firstname", "Lastname")
    private val pairingPartner = PairingPartner.SHUBHI
    private val pairingPartner1 = PairingPartner.SHUBHI
    private val pairingPartner2 = PairingPartner.BERNHARD

    @Test
    fun opens_two_pull_requests_for_different_iterations_and_pairing_partner() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner(pairingPartner1)
                .build()
        val branch2 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(2)
                .with_pairing_partner(pairingPartner2)
                .build()
        val (pullRequests, sut) = create_github_pr_factory(listOf(branch1, branch2))

        sut.open_pull_requests(candidate, listOf(pairingPartner1, pairingPartner2))

        assertThat(pullRequests).containsExactly(
                PullRequest(
                        title = "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestNames[0]} [PR]",
                        _base = Branch("master"),
                        _head = branch1),
                PullRequest(
                        title = "Firstname Lastname Iteration 2 / Session 2 ${pairingPartner2.pullRequestNames[0]}",
                        _base = branch1,
                        _head = branch2))
    }

    @Test
    fun opens_pull_request_and_ignores_if_candidates_first_name_is_capitalized() {
        val candidate = Candidate("Firstname", "lastname")
        val branch = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner(pairingPartner)
                .build()
        val (pullRequests, sut) = create_github_pr_factory(listOf(branch))

        sut.open_pull_requests(candidate, listOf(pairingPartner))

        assertThat(pullRequests).containsExactly(PullRequest(
                title = "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestNames[0]}",
                _base = Branch("master"),
                _head = branch))
    }

    @Test
    fun opens_pull_request_and_ignores_if_candidates_last_name_is_capitalized() {
        val candidate = Candidate("firstname", "Lastname")
        val branch = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner(pairingPartner)
                .build()
        val (pullRequests, sut) = create_github_pr_factory(listOf(branch))

        sut.open_pull_requests(candidate, listOf(pairingPartner))

        assertThat(pullRequests).containsExactly(PullRequest(
                title = "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestNames[0]}",
                _base = Branch("master"),
                _head = branch))
    }

    @Test
    fun opens_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_first_name() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                TestBranchBuilder()
                        .with_candidate(Candidate("Firstname", "Lastname"))
                        .with_iteration(1)
                        .with_pairing_partner(pairingPartner)
                        .build()))

        sut.open_pull_requests(Candidate("firstname2", "Lastname"), listOf(pairingPartner))

        assertThat(pullRequests).isEmpty()
    }

    @Test
    fun opens_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_last_name() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                TestBranchBuilder()
                        .with_candidate(Candidate("Firstname", "Lastname"))
                        .with_iteration(1)
                        .with_pairing_partner(pairingPartner1)
                        .build()))

        sut.open_pull_requests(Candidate("Firstname", "lastname2"), listOf(pairingPartner))

        assertThat(pullRequests).isEmpty()
    }

    @Test
    fun branch_can_not_be_processed_if_branch_name_has_invalid_name() {
        val sut = create_github_pr_factory_for(INVALID_BRANCH_NAME)

        assertThatThrownBy { sut.open_pull_requests(candidate, listOf(pairingPartner)) }
                .hasMessageContaining("invalid name")
                .hasMessageContaining(INVALID_BRANCH_NAME)
    }

    @Test
    fun close_pull_requests_for_two_candidates_with_same_first_name() {
        val (pullRequestNumbersToBeClosed, sut) = create_github_pr_factory_for(listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}"),
                GetPullRequest(2, "Firstname1 Lastname2 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}")))

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1)
    }

    @Test
    fun close_pull_requests_for_two_candidates_with_same_last_name() {
        val (pullRequestNumbersToBeClosed, sut) = create_github_pr_factory_for(listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}"),
                GetPullRequest(2, "Firstname2 Lastname1 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}")))

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1)
    }

    @Test
    fun close_pull_requests_for_one_candidate() {
        val (pullRequestNumbersToBeClosed, sut) = create_github_pr_factory_for(listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 ${pairingPartner1.name.toLowerCase()}"),
                GetPullRequest(2, "Firstname1 Lastname1 Iteration 1 / Session 2 ${pairingPartner2.name.toLowerCase()}")))

        sut.close_pull_requests_for(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
                .describedAs("Expected pull request numbers to be closed")
                .containsExactly(1, 2)
    }

    private fun create_github_pr_factory(branches: List<Branch>): Pair<MutableList<PullRequest>, GithubPRFactory> {
        val pullRequests = mutableListOf<PullRequest>()
        val githubBranchesRepo = github_branches_repo(branches)
        val sut = GithubPRFactory(
                ConsoleUI(),
                githubBranchesRepo,
                github_pull_requests_repo(github_write_repo(pullRequests)),
                BranchSyntaxValidator(QuietUI()),
                PullRequestLastNotFinishedMarker())
        return Pair(pullRequests, sut)
    }

    private fun create_github_pr_factory_for(pullRequests: List<GetPullRequest>): Pair<MutableList<Int>, GithubPRFactory> {
        val pullRequestNumbersToBeClosed = mutableListOf<Int>()
        val sut = GithubPRFactory(
                ConsoleUI(),
                github_branches_repo(emptyList()),
                github_pull_requests_repo(
                        github_write_repo(mutableListOf(), pullRequestNumbersToBeClosed),
                        pullRequests.toMutableList()),
                BranchSyntaxValidator(QuietUI()),
                PullRequestLastNotFinishedMarker())
        return Pair(pullRequestNumbersToBeClosed, sut)
    }

    private fun create_github_pr_factory_for(branchName: String) =
            GithubPRFactory(
                    ConsoleUI(),
                    github_branches_repo(listOf(Branch(branchName))),
                    github_pull_requests_repo(noop_github_write_repo()),
                    BranchSyntaxValidator(QuietUI()),
                    PullRequestLastNotFinishedMarker())

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
