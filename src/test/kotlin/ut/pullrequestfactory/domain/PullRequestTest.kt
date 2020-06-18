package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.PullRequest

class PullRequestTest {
    @Test
    fun mark_current_pull_request_when_next_pull_request_has_new_iteration() {
        val sut = PullRequest(
                _title = "Firstname Lastname Iteration 1 / Session 1 pairingpartner",
                _base = Branch("master"),
                _head = Branch("firstname_lastname_iteration_1_pairingpartner"))

        val nextPr = PullRequest(
                _title = "Firstname Lastname Iteration 2 / Session 1 pairingpartner",
                _base = Branch("firstname_lastname_iteration_1_pairingpartner"),
                _head = Branch("firstname_lastname_iteration_2_pairingpartner"))

        sut.add_pr_mark_to_title(nextPr = nextPr)

        assertThat(sut.title).isEqualTo("Firstname Lastname Iteration 1 / Session 1 pairingpartner [PR]")
    }
}
