package pullrequestfactory.domain

data class PullRequest(
        private var _title: String,
        private val _base: Branch,
        private val _head: Branch) {

    val title: String get() = _title
    val base: String get() = _base.name
    val head: String get() = _head.name

    fun add_pr_mark_to_title(nextPr: PullRequest) {
        _title = if (nextPr.is_new_iteration()) "$_title [PR]" else _title
    }

    private fun is_new_iteration(): Boolean =
            _base.is_not_master() && _base.iteration_nr() < _head.iteration_nr()

}
