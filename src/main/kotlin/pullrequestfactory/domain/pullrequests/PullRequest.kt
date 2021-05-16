package pullrequestfactory.domain.pullrequests

import kotlinx.serialization.Serializable
import pullrequestfactory.domain.branches.Branch

@Serializable
data class PullRequest(
    val title: String,
    val _base: Branch,
    val _head: Branch
) {

    val base: String = _base.name
    val head: String = _head.name

    fun markTitleWhenNextHasNewIteration(nextPr: PullRequest) = when {
        nextPr.hasNewIteration() -> markTitle()
        else -> this.copy()
    }

    private fun hasNewIteration() = _base.iterationNr() < _head.iterationNr()

    fun markTitle() = this.copy(title = "$title [PR]")

}
