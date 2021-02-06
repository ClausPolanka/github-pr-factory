package ut.pullrequestfactory.domain.pullrequests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.Branches
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import ut.pullrequestfactory.domain.TestBranchBuilder

class CreatePullRequestsForAListOfBranches {

    private val pairingPartner = PairingPartner.SHUBHI
    private val pairingPartner1 = PairingPartner.SHUBHI
    private val pairingPartner2 = PairingPartner.BERNI

    @Test
    fun create_pull_request_for_one_session_within_same_iteration_for_same_pairing_partner() {
        val branch = TestBranchBuilder()
            .withCandidate(Candidate("Firstname", "Lastname"))
            .withIteration(1)
            .withPairingPartner(pairingPartner)
            .build()
        val sut = create_branches_for(listOf(branch))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner))

        assertThat(prs).containsExactly(
            PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestName()}",
                Branch("master"),
                branch
            )
        )
    }

    @Test
    fun create_pull_requests_for_one_session_and_two_iterations_for_same_pairing_partner() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner)
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner(pairingPartner)
            .build()
        val sut = create_branches_for(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner))

        assertThat(prs)
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestName()} [PR]",
                    Branch("master"),
                    branch1
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner.pullRequestName()}",
                    branch1,
                    branch2
                )
            )
    }

    @Test
    fun create_pull_requests_for_two_sessions_within_same_iteration_for_different_pairing_partner() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner1)
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner2)
            .build()
        val sut = create_branches_for(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs)
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestName()}",
                    Branch("master"),
                    branch1
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 2 ${pairingPartner2.pullRequestName()}",
                    branch1,
                    branch2
                )
            )
    }

    @Test
    fun create_pull_requests_for_two_sessions_for_two_iterations_for_different_pairing_partner() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner("shubhi")
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner("berni")
            .build()
        val sut = create_branches_for(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(listOf(PairingPartner.SHUBHI, PairingPartner.BERNI))

        assertThat(prs)
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 1 Shubhi [PR]",
                    Branch("master"),
                    branch1
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 2 / Session 2 Berni",
                    branch1,
                    branch2
                )
            )
    }

    @Test
    fun create_pull_requests_for_two_different_sessions_where_in_first_session_a_new_iteration_is_started() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner1)
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner(pairingPartner1)
            .build()
        val branch3 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner(pairingPartner2)
            .build()
        val sut = create_branches_for(listOf(branch1, branch2, branch3))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs)
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestName()} [PR]",
                    Branch("master"),
                    branch1
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner1.pullRequestName()}",
                    branch1,
                    branch2
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 2 / Session 2 ${pairingPartner2.pullRequestName()}",
                    branch2,
                    branch3
                )
            )
    }

    @Test
    fun support_multiple_sessions_for_same_pairing_partner() {
        val branch1 = TestBranchBuilder()
            .with_branch_name("firstname_lastname_Iteration_1_tomas")
            .build()
        val branch2 = TestBranchBuilder()
            .with_branch_name("firstname_lastname_Iteration_1_shubi")
            .build()
        val branch3 = TestBranchBuilder()
            .with_branch_name("firstname_lastname_Iteration_1_tomasr")
            .build()
        val sut = create_branches_for(listOf(branch1, branch2, branch3))

        val prs = sut.getPullRequestsFor(
            listOf(
                PairingPartner.TOMAS,
                PairingPartner.SHUBHI,
                PairingPartner.TOMAS
            )
        )

        assertThat(prs)
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 1 Tomas",
                    Branch("master"),
                    branch1
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 2 Shubhi",
                    branch1,
                    branch2
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 3 Tomas",
                    branch2,
                    branch3
                )
            )
    }

    @Test
    fun create_pull_requests_where_branches_are_in_wrong_order_compared_to_given_pairing_partner() {
        val branch1 = TestBranchBuilder()
            .with_branch_name("firstname_lastname_Iteration_1_berni")
            .build()
        val branch2 = TestBranchBuilder()
            .with_branch_name("firstname_lastname_Iteration_1_claus")
            .build()
        val sut = create_branches_for(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(
            listOf(
                PairingPartner.CLAUS,
                PairingPartner.BERNI
            )
        )

        assertThat(prs)
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 1 Claus",
                    Branch("master"),
                    branch2
                )
            )
            .contains(
                PullRequest(
                    "Firstname Lastname Iteration 1 / Session 2 Berni",
                    branch2,
                    branch1
                )
            )
    }

    private fun create_branches_for(branches: List<Branch>) = Branches(branches, PullRequestLastNotFinishedMarker())

}
