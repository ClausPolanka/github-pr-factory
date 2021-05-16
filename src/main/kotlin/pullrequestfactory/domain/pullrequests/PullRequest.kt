package pullrequestfactory.domain.pullrequests

import kotlinx.serialization.Serializable
import pullrequestfactory.domain.branches.Branch

data class PullRequest(
    val title: String,
    val base: Branch,
    val head: Branch
) {

    fun markTitleWhenNextHasNewIteration(nextPr: PullRequest) = when {
        nextPr.hasNewIteration() -> markTitle()
        else -> this.copy()
    }

    private fun hasNewIteration() = base.iterationNr() < head.iterationNr()

    fun markTitle() = this.copy(title = "$title [PR]")

}
