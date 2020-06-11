package pullrequestfactory.domain

data class PullRequest(var title: String, val base: String, val head: String) {

    fun add_pr_mark_to_title(nextPr: PullRequest) {
        title = if (nextPr.is_pull_request()) "$title [PR]" else title
    }

    private fun is_pull_request(): Boolean = !is_base_master() && Branch(base).iteration_nr() < Branch(head).iteration_nr()

    private fun is_base_master(): Boolean = base == "master"

}
