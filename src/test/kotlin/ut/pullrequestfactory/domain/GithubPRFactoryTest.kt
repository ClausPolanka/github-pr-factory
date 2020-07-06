package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.*

private const val invalidBranchName = "firstname_lastname_claus"
private val candidate = Candidate("Firstname", "Lastname")
private val pairingpartner = listOf("Pairingpartner")

class GithubPRFactoryTest {

    @Test
    fun creates_two_pull_requests_for_different_iterations_and_pairing_partner() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")))

        sut.create_pull_requests(Candidate("Firstname", "Lastname"), listOf("Pairingpartner1", "Pairingpartner2"))

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
    fun creates_pull_request_and_ignores_if_candidates_first_name_is_capitalized() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner")))

        sut.create_pull_requests(Candidate(firstName = "Firstname", lastName = "lastname"), listOf("Pairingpartner"))

        assertThat(pullRequests).containsExactly(PullRequest(
                _title = "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                _base = Branch("master"),
                _head = Branch("firstname_lastname_iteration_1_pairingpartner")))
    }

    @Test
    fun creates_pull_request_and_ignores_if_candidates_last_name_is_capitalized() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner")))

        sut.create_pull_requests(Candidate("firstname", "Lastname"), listOf("Pairingpartner"))

        assertThat(pullRequests).containsExactly(PullRequest(
                _title = "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                _base = Branch("master"),
                _head = Branch("firstname_lastname_iteration_1_pairingpartner")))
    }

    @Test
    fun creates_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_first_name() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname1_lastname_iteration_1_pairingpartner")))

        sut.create_pull_requests(Candidate("firstname2", "Lastname"), listOf("Pairingpartner"))

        assertThat(pullRequests).isEmpty()
    }


    @Test
    fun creates_no_pull_requests_for_candidate_when_no_branch_exists_containing_candidates_last_name() {
        val (pullRequests, sut) = create_github_pr_factory(listOf(
                Branch("firstname_lastname1_iteration_1_pairingpartner")))

        sut.create_pull_requests(Candidate("Firstname", "lastname2"), listOf("Pairingpartner"))

        assertThat(pullRequests).isEmpty()
    }

    @Test
    fun branch_can_not_be_processed_if_branch_name_has_invalid_name() {
        val sut = create_github_pr_factory_for(invalidBranchName)

        assertThatThrownBy { sut.create_pull_requests(candidate, pairingpartner) }
                .hasMessageContaining("invalid name")
                .hasMessageContaining(invalidBranchName)
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
        val githubReadRepo = github_read_repo(branches, emptyList())
        val sut = GithubPRFactory(githubReadRepo, github_write_repo(pullRequests, mutableListOf()), BranchSyntaxValidator(QuietUI()))
        return Pair(pullRequests, sut)
    }

    private fun create_github_pr_factory_for(pullRequests: List<GetPullRequest>): Pair<MutableList<Int>, GithubPRFactory> {
        val pullRequestNumbersToBeClosed = mutableListOf<Int>()
        val sut = GithubPRFactory(
                github_read_repo(emptyList(), pullRequests),
                github_write_repo(mutableListOf(), pullRequestNumbersToBeClosed),
                BranchSyntaxValidator(QuietUI()))
        return Pair(pullRequestNumbersToBeClosed, sut)
    }

    private fun create_github_pr_factory_for(branchName: String) =
            GithubPRFactory(
                    github_read_repo(listOf(Branch(branchName)), emptyList()),
                    noop_github_write_repo(),
                    BranchSyntaxValidator(QuietUI()))

    private fun github_read_repo(branches: List<Branch>, pullRequests: List<GetPullRequest>): GithubReadRepo {
        return object : GithubReadRepo {
            override fun get_all_branches(): List<Branch> {
                return branches
            }

            override fun get_all_open_pull_requests(): List<GetPullRequest> {
                return pullRequests
            }
        }
    }

    private fun github_write_repo(expectedPrs: MutableList<PullRequest>, expectedPullRequestNumbersToBeClosed: MutableList<Int>): GithubWriteRepo {
        return object : GithubWriteRepo {
            override fun create_pull_request(pullRequest: PullRequest) {
                expectedPrs.add(pullRequest)
            }

            override fun close_pull_request(number: Int) {
                expectedPullRequestNumbersToBeClosed.add(number)
            }
        }
    }

    private fun noop_github_write_repo() = object : GithubWriteRepo {
        override fun create_pull_request(pullRequest: PullRequest) {
            // can be ignored in this test
        }

        override fun close_pull_request(number: Int) {
            // can be ignored in this test
        }
    }

}
