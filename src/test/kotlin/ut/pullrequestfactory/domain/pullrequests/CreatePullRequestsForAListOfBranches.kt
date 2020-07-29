package ut.pullrequestfactory.domain.pullrequests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.Branches
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker

class CreatePullRequestsForAListOfBranches {

    private val pairingPartner = PairingPartner.SHUBI
    private val pairingPartner1 = PairingPartner.SHUBI
    private val pairingPartner2 = PairingPartner.BERNI

    @Test
    fun create_pull_request_for_one_session_within_same_iteration_for_same_pairing_partner() {
        val prs = Branches(
                listOf(Branch("firstname_lastname_iteration_1_${pairingPartner.name.toLowerCase()}")),
                PullRequestLastNotFinishedMarker()).pull_requests_for(listOf(pairingPartner))

        assertThat(prs).containsExactly(PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.nickName}",
                Branch("master"),
                Branch("firstname_lastname_iteration_1_${pairingPartner.name.toLowerCase()}")))
    }

    @Test
    fun create_pull_requests_for_one_session_and_two_iterations_for_same_pairing_partner() {
        val prs = Branches(listOf(
                Branch("firstname_lastname_iteration_1_${pairingPartner.name.toLowerCase()}"),
                Branch("firstname_lastname_iteration_2_${pairingPartner.name.toLowerCase()}")),
                PullRequestLastNotFinishedMarker()).pull_requests_for(listOf(pairingPartner))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.nickName} [PR]",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_${pairingPartner.name.toLowerCase()}")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner.nickName}",
                        Branch("firstname_lastname_iteration_1_${pairingPartner.name.toLowerCase()}"),
                        Branch("firstname_lastname_iteration_2_${pairingPartner.name.toLowerCase()}")))
    }

    @Test
    fun create_pull_requests_for_two_sessions_within_same_iteration_for_different_pairing_partner() {
        val prs = Branches(listOf(
                Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}"),
                Branch("firstname_lastname_iteration_1_${pairingPartner2.name.toLowerCase()}")),
                PullRequestLastNotFinishedMarker()).pull_requests_for(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.nickName}",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 2 ${pairingPartner2.nickName}",
                        Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}"),
                        Branch("firstname_lastname_iteration_1_${pairingPartner2.name.toLowerCase()}")))
    }

    @Test
    fun create_pull_requests_for_two_sessions_for_two_iterations_for_different_pairing_partner() {
        val prs = Branches(listOf(
                Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}"),
                Branch("firstname_lastname_iteration_2_${pairingPartner2.name.toLowerCase()}")),
                PullRequestLastNotFinishedMarker()).pull_requests_for(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.nickName} [PR]",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 ${pairingPartner2.nickName}",
                        Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}"),
                        Branch("firstname_lastname_iteration_2_${pairingPartner2.name.toLowerCase()}")))
    }

    @Test
    fun create_pull_requests_for_two_different_sessions_where_in_first_session_a_new_iteration_is_started() {
        val prs = Branches(listOf(
                Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}"),
                Branch("firstname_lastname_iteration_2_${pairingPartner1.name.toLowerCase()}"),
                Branch("firstname_lastname_iteration_2_${pairingPartner2.name.toLowerCase()}")),
                PullRequestLastNotFinishedMarker()).pull_requests_for(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.nickName} [PR]",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner1.nickName}",
                        Branch("firstname_lastname_iteration_1_${pairingPartner1.name.toLowerCase()}"),
                        Branch("firstname_lastname_iteration_2_${pairingPartner1.name.toLowerCase()}")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 ${pairingPartner2.nickName}",
                        Branch("firstname_lastname_iteration_2_${pairingPartner1.name.toLowerCase()}"),
                        Branch("firstname_lastname_iteration_2_${pairingPartner2.name.toLowerCase()}")))
    }

}
