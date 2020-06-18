package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.PullRequest

private const val prTitle = "Firstname Lastname Iteration 1 / Session 1 pairingpartner"

class PullRequestTest {

    @Test
    fun mark_title_of_current_pull_request_when_next_pull_request_has_new_iteration_and_current_base_is_master() {
        val sut = create_pull_request_with(prTitle)

        sut.add_pr_mark_to_title(nextPr = PullRequest(
                _title = "any",
                _base = create_branch_for(iterationNr = 1),
                _head = create_branch_for(iterationNr = 2)))

        assertThat(sut.title).isEqualTo("$prTitle [PR]")
    }

    @Test
    fun mark_title_of_current_pull_request_when_next_pull_request_has_new_iteration_and_base_is_not_master() {
        val sut = PullRequest(
                _title = "Firstname Lastname Iteration 2 / Session 1 pairingpartner",
                _base = Branch("firstname_lastname_iteration_1_pairingpartner"),
                _head = Branch("firstname_lastname_iteration_2_pairingpartner"))

        val nextPr = PullRequest(
                _title = "Firstname Lastname Iteration 3 / Session 1 pairingpartner",
                _base = Branch("firstname_lastname_iteration_2_pairingpartner"),
                _head = Branch("firstname_lastname_iteration_3_pairingpartner"))

        sut.add_pr_mark_to_title(nextPr = nextPr)

        assertThat(sut.title).isEqualTo("Firstname Lastname Iteration 2 / Session 1 pairingpartner")
    }

    @Test
    fun keep_title_of_current_pull_request_when_next_pull_request_has_same_iteration() {
        val sut = PullRequest(
                _title = "Firstname Lastname Iteration 1 / Session 1 pairingpartner1",
                _base = Branch("master"),
                _head = Branch("firstname_lastname_iteration_1_pairingpartner1"))

        val nextPr = PullRequest(
                _title = "Firstname Lastname Iteration 1 / Session 2 pairingpartner2",
                _base = Branch("firstname_lastname_iteration_1_pairingpartner1"),
                _head = Branch("firstname_lastname_iteration_1_pairingpartner2"))

        sut.add_pr_mark_to_title(nextPr = nextPr)

        assertThat(sut.title).isEqualTo("Firstname Lastname Iteration 1 / Session 1 pairingpartner1")
    }

    private fun create_pull_request_with(title: String): PullRequest {
        return PullRequest(
                _title = title,
                _base = Branch("any"),
                _head = Branch("any"))
    }

    private fun create_branch_for(iterationNr: Int) =
            Branch("firstname_lastname_iteration_${iterationNr}_pairingpartner")

}
