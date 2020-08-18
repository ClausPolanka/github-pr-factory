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
    private val pairingPartner2 = PairingPartner.BERNHARD

    @Test
    fun create_pull_request_for_one_session_within_same_iteration_for_same_pairing_partner() {
        val branch = TestBranchBuilder()
                .with_candidate(Candidate("Firstname", "Lastname"))
                .with_iteration(1)
                .with_pairing_partner(pairingPartner)
                .build()
        val sut = create_branches_for(listOf(branch))

        val prs = sut.get_pull_requests_for(listOf(pairingPartner))

        assertThat(prs).containsExactly(PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestNames[0]}",
                Branch("master"),
                branch))
    }

    @Test
    fun create_pull_requests_for_one_session_and_two_iterations_for_same_pairing_partner() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner(pairingPartner)
                .build()
        val branch2 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(2)
                .with_pairing_partner(pairingPartner)
                .build()
        val sut = create_branches_for(listOf(branch1, branch2))

        val prs = sut.get_pull_requests_for(listOf(pairingPartner))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestNames[0]} [PR]",
                        Branch("master"),
                        branch1))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner.pullRequestNames[0]}",
                        branch1,
                        branch2))
    }

    @Test
    fun create_pull_requests_for_two_sessions_within_same_iteration_for_different_pairing_partner() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner(pairingPartner1)
                .build()
        val branch2 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner(pairingPartner2)
                .build()
        val sut = create_branches_for(listOf(branch1, branch2))

        val prs = sut.get_pull_requests_for(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestNames[0]}",
                        Branch("master"),
                        branch1))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 2 ${pairingPartner2.pullRequestNames[0]}",
                        branch1,
                        branch2))
    }

    @Test
    fun create_pull_requests_for_two_sessions_for_two_iterations_for_different_pairing_partner() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner("shubhi")
                .build()
        val branch2 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(2)
                .with_pairing_partner("berni")
                .build()
        val sut = create_branches_for(listOf(branch1, branch2))

        val prs = sut.get_pull_requests_for(listOf(PairingPartner.SHUBHI, PairingPartner.BERNHARD))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Shubhi [PR]",
                        Branch("master"),
                        branch1))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 Berni",
                        branch1,
                        branch2))
    }

    @Test
    fun create_pull_requests_for_two_different_sessions_where_in_first_session_a_new_iteration_is_started() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(1)
                .with_pairing_partner(pairingPartner1)
                .build()
        val branch2 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(2)
                .with_pairing_partner(pairingPartner1)
                .build()
        val branch3 = TestBranchBuilder()
                .with_candidate(candidate)
                .with_iteration(2)
                .with_pairing_partner(pairingPartner2)
                .build()
        val sut = create_branches_for(listOf(branch1, branch2, branch3))

        val prs = sut.get_pull_requests_for(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestNames[0]} [PR]",
                        Branch("master"),
                        branch1))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner1.pullRequestNames[0]}",
                        branch1,
                        branch2))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 ${pairingPartner2.pullRequestNames[0]}",
                        branch2,
                        branch3))
    }

    @Test
    fun support_multiple_sessions_for_same_pairing_partner() {
        val branch1 = TestBranchBuilder()
                .with_branch_name("firstname_lastname_Iteration_1_tomas")
                .build()
        val branch2 = TestBranchBuilder()
                .with_branch_name("firstname_lastname_Iteration_1_claus")
                .build()
        val branch3 = TestBranchBuilder()
                .with_branch_name("firstname_lastname_Iteration_1_tomasr")
                .build()
        val sut = create_branches_for(listOf(branch1, branch2, branch3))

        val prs = sut.get_pull_requests_for(listOf(
                PairingPartner.TOMAS,
                PairingPartner.CLAUS,
                PairingPartner.TOMAS))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Tomas",
                        Branch("master"),
                        branch1))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 2 Claus",
                        branch1,
                        branch2))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 3 Tomas",
                        branch2,
                        branch3))
    }

    private fun create_branches_for(branches: List<Branch>) = Branches(branches, PullRequestLastNotFinishedMarker())

}
