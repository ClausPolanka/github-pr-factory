package pullrequestfactory.domain.pullrequests

import pullrequestfactory.domain.branches.Branch

data class PullRequest(
        val title: String,
        private val _base: Branch,
        private val _head: Branch) {

    val base: String get() = _base.name
    val head: String get() = _head.name

    fun mark_title_finished_when_next_pull_request_has_new_iteration(nextPr: PullRequest) = when {
        nextPr.has_new_iteration() -> mark_title_finished()
        else -> this.copy()
    }

    fun mark_title_finished() = this.copy(title = "$title [PR]")

    private fun has_new_iteration() = _base.iteration_nr() < _head.iteration_nr()

}
