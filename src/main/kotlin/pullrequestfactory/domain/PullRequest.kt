package pullrequestfactory.domain

data class PullRequest(private var _title: String, val base: String, val head: String) {

    val title: String get() = _title

    fun add_pr_mark_to_title(nextPr: PullRequest) {
        _title = if (nextPr.is_new_iteration()) "$_title [PR]" else _title
    }

    private fun is_new_iteration(): Boolean = !is_base_master() && Branch(base).iteration_nr() < Branch(head).iteration_nr()

    private fun is_base_master(): Boolean = base == "master"

}
