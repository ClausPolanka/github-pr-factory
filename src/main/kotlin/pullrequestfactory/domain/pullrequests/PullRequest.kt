package pullrequestfactory.domain.pullrequests

import pullrequestfactory.domain.branches.Branch

data class PullRequest(
    val title: String,
    private val _base: Branch,
    private val _head: Branch
) {

    val base: String get() = _base.name
    val head: String get() = _head.name

    fun markTitleWhenNextHasNewIteration(nextPr: PullRequest) = when {
        nextPr.hasNewIteration() -> markTitle()
        else -> this.copy()
    }

    private fun hasNewIteration() = _base.iterationNr() < _head.iterationNr()

    fun markTitle() = this.copy(title = "$title [PR]")

}
