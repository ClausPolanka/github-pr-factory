package ut.pullrequestfactory.domain.pullrequests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest


class PullRequestTest {

    private val pullRequestTitle = "Firstname Lastname Iteration 1 / Session 1 pairingpartner"

    @Test
    fun `mark title of current pull request when next pull request has new iteration`() {
        val sut = createPullRequest()

        val newPr = sut.markTitleWhenNextHasNewIteration(
            nextPr = PullRequest(
                title = "any",
                base = createBranchFor(iterationNr = 1),
                head = createBranchFor(iterationNr = 2)
            )
        )

        assertThat(newPr.title).isEqualTo("${sut.title} [PR]")
    }

    @Test
    fun `keep title of current pull request when next pull request has same iteration`() {
        val sut = createPullRequest()

        val newPr = sut.markTitleWhenNextHasNewIteration(
            nextPr = PullRequest(
                title = "any",
                base = createBranchFor(iterationNr = 1),
                head = createBranchFor(iterationNr = 1)
            )
        )

        assertThat(newPr.title).isEqualTo(sut.title)
    }

    private fun createPullRequest() =
        PullRequest(pullRequestTitle, base = Branch("any"), head = Branch("any"))

    private fun createBranchFor(iterationNr: Int) =
        Branch("firstname_lastname_iteration_${iterationNr}_pairingpartner")

}
