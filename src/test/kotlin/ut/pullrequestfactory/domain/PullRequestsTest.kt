package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.domain.PullRequests

class PullRequestsTest {

    @Test
    fun create_pull_request_for_one_session_within_same_iteration_for_same_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(
                listOf(Branch("firstname_lastname_iteration_1_pairingpartner")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles).containsExactly(PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                "master",
                "firstname_lastname_iteration_1_pairingpartner"))
    }

    @Test
    fun create_pull_requests_for_one_session_and_two_iterations_for_same_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner"),
                Branch("firstname_lastname_iteration_2_pairingpartner")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner",
                        "master",
                        "firstname_lastname_iteration_1_pairingpartner"))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 Pairingpartner",
                        "firstname_lastname_iteration_1_pairingpartner",
                        "firstname_lastname_iteration_2_pairingpartner"))
    }

    @Test
    fun create_pull_requests_for_two_sessions_within_same_iteration_for_different_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_1_pairingpartner2")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner1",
                        "master",
                        "firstname_lastname_iteration_1_pairingpartner1"))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 2 Pairingpartner2",
                        "firstname_lastname_iteration_1_pairingpartner1",
                        "firstname_lastname_iteration_1_pairingpartner2"))
    }

    @Test
    fun create_pull_requests_for_two_sessions_for_two_iterations_for_different_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner1",
                        "master",
                        "firstname_lastname_iteration_1_pairingpartner1"))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 Pairingpartner2",
                        "firstname_lastname_iteration_1_pairingpartner1",
                        "firstname_lastname_iteration_2_pairingpartner2"))
    }

    @Test
    fun create_pull_requests_for_two_different_sessions_where_in_first_session_a_new_iteration_is_started() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles)
                .contains(PullRequest(
                        "Firstname Lastname Iteration 1 / Session 1 Pairingpartner1",
                        "master",
                        "firstname_lastname_iteration_1_pairingpartner1"))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 1 Pairingpartner1",
                        "firstname_lastname_iteration_1_pairingpartner1",
                        "firstname_lastname_iteration_2_pairingpartner1"))
                .contains(PullRequest(
                        "Firstname Lastname Iteration 2 / Session 2 Pairingpartner2",
                        "firstname_lastname_iteration_2_pairingpartner1",
                        "firstname_lastname_iteration_2_pairingpartner2"))
    }

}
