package ut.pullrequestfactory.domain.pullrequests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.pullrequests.PullRequests

class PullRequestsTest {

    @Test
    fun create_pull_request_for_one_session_within_same_iteration_for_same_pairing_partner() {
        val sut = createPullRequests()

        val prs = sut.create_pull_requests_for(
                listOf(Branch("firstname_lastname_iteration_1_pairingpartner")))

        assertThat(prs).containsExactly(PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                Branch("master"),
                Branch("firstname_lastname_iteration_1_pairingpartner")))
    }

    @Test
    fun create_pull_requests_for_one_session_and_two_iterations_for_same_pairing_partner() {
        val sut = createPullRequests()

        val prs = sut.create_pull_requests_for(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner"),
                Branch("firstname_lastname_iteration_2_pairingpartner")))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner [PR]",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_pairingpartner")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 Pairingpartner",
                        Branch("firstname_lastname_iteration_1_pairingpartner"),
                        Branch("firstname_lastname_iteration_2_pairingpartner")))
    }

    @Test
    fun create_pull_requests_for_two_sessions_within_same_iteration_for_different_pairing_partner() {
        val sut = createPullRequests()

        val prs = sut.create_pull_requests_for(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_1_pairingpartner2")))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner1",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_pairingpartner1")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 2 Pairingpartner2",
                        Branch("firstname_lastname_iteration_1_pairingpartner1"),
                        Branch("firstname_lastname_iteration_1_pairingpartner2")))
    }

    @Test
    fun create_pull_requests_for_two_sessions_for_two_iterations_for_different_pairing_partner() {
        val sut = createPullRequests()

        val prs = sut.create_pull_requests_for(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner1 [PR]",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_pairingpartner1")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 Pairingpartner2",
                        Branch("firstname_lastname_iteration_1_pairingpartner1"),
                        Branch("firstname_lastname_iteration_2_pairingpartner2")))
    }

    @Test
    fun create_pull_requests_for_two_different_sessions_where_in_first_session_a_new_iteration_is_started() {
        val sut = createPullRequests()

        val prs = sut.create_pull_requests_for(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")))

        assertThat(prs)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner1 [PR]",
                        Branch("master"),
                        Branch("firstname_lastname_iteration_1_pairingpartner1")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 Pairingpartner1",
                        Branch("firstname_lastname_iteration_1_pairingpartner1"),
                        Branch("firstname_lastname_iteration_2_pairingpartner1")))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 Pairingpartner2",
                        Branch("firstname_lastname_iteration_2_pairingpartner1"),
                        Branch("firstname_lastname_iteration_2_pairingpartner2")))
    }

    private fun createPullRequests() =
            PullRequests(PullRequestLastNotFinishedMarker())

}