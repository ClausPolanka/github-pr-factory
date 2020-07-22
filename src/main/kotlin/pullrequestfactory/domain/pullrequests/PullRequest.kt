package pullrequestfactory.domain.pullrequests

import pullrequestfactory.domain.branches.Branch

data class PullRequest(
        private var _title: String,
        private val _base: Branch,
        private val _head: Branch) {

    val title: String get() = _title
    val base: String get() = _base.name
    val head: String get() = _head.name

    fun mark_title_finished_when_next_pull_request_has_new_iteration(nextPr: PullRequest): PullRequest {
        return when {
            nextPr.has_new_iteration() -> mark_title_finished()
            else -> this.copy()
        }
    }

    fun mark_title_finished(): PullRequest {
        return this.copy(_title = "$_title [PR]")
    }

    private fun has_new_iteration(): Boolean = _base.iteration_nr() < _head.iteration_nr()

}
