package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.domain.PullRequests

class PullRequestsTest {

    @Test
    fun create_pull_request_title_for_one_session_within_same_iteration_for_same_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(
                listOf(Branch("firstname_lastname_iteration_1_pairingpartner")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles).containsExactly(PullRequest("Firstname Lastname Iteration 1 / Session 1 Pairingpartner"))
    }

    @Test
    fun create_pull_request_titles_for_one_session_and_two_iterations_for_same_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner"),
                Branch("firstname_lastname_iteration_2_pairingpartner")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles)
                .contains(PullRequest("Firstname Lastname Iteration 1 / Session 1 Pairingpartner"))
                .contains(PullRequest("Firstname Lastname Iteration 2 / Session 1 Pairingpartner"))
    }

    @Test
    fun create_pull_request_titles_for_two_sessions_within_same_iteration_for_different_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_1_pairingpartner2")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles)
                .contains(PullRequest("Firstname Lastname Iteration 1 / Session 1 Pairingpartner1"))
                .contains(PullRequest("Firstname Lastname Iteration 1 / Session 2 Pairingpartner2"))
    }

    @Test
    fun create_pull_request_titles_for_two_sessions_for_two_iterations_for_different_pairing_partner() {
        val sut = PullRequests()

        val prTitles = sut.create_pull_requests(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner2")),
                Candidate("Firstname", "Lastname"))

        assertThat(prTitles)
                .contains(PullRequest("Firstname Lastname Iteration 1 / Session 1 Pairingpartner1"))
                .contains(PullRequest("Firstname Lastname Iteration 2 / Session 2 Pairingpartner2"))
    }

}
