package ut.pullrequestfactory.domain.pullrequests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest


class PullRequestTest {

    private val PULL_REQUEST_TITLE = "Firstname Lastname Iteration 1 / Session 1 pairingpartner"

    @Test
    fun mark_title_of_current_pull_request_when_next_pull_request_has_new_iteration() {
        val sut = create_pull_request_with(PULL_REQUEST_TITLE)

        val newPr = sut.mark_title_when_next_has_new_iteration(nextPr = PullRequest(
                title = "any",
                _base = create_branch_for(iterationNr = 1),
                _head = create_branch_for(iterationNr = 2)))

        assertThat(newPr.title).isEqualTo("$PULL_REQUEST_TITLE [PR]")
    }

    @Test
    fun keep_title_of_current_pull_request_when_next_pull_request_has_same_iteration() {
        val sut = create_pull_request_with(PULL_REQUEST_TITLE)

        val newPr = sut.mark_title_when_next_has_new_iteration(nextPr = PullRequest(
                title = "any",
                _base = create_branch_for(iterationNr = 1),
                _head = create_branch_for(iterationNr = 1)))

        assertThat(newPr.title).isEqualTo(PULL_REQUEST_TITLE)
    }

    private fun create_pull_request_with(title: String): PullRequest {
        return PullRequest(title = title, _base = Branch("any"), _head = Branch("any"))
    }

    private fun create_branch_for(iterationNr: Int) =
            Branch("firstname_lastname_iteration_${iterationNr}_pairingpartner")

}
