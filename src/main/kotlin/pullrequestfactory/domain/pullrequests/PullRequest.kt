package pullrequestfactory.domain.pullrequests

import pullrequestfactory.domain.branches.Branch

data class PullRequest(
        private var _title: String,
        private val _base: Branch,
        private val _head: Branch) {

    val title: String get() = _title
    val base: String get() = _base.name
    val head: String get() = _head.name

    private val emptyPr = PullRequest("", Branch(""), Branch(""))

    fun mark_title_finished(nextPr: PullRequest = emptyPr) {
        _title = if (nextPr.has_new_iteration()) "$_title [PR]" else _title
    }

    private fun has_new_iteration(): Boolean = _base.iteration_nr() < _head.iteration_nr()

}
