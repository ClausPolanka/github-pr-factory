package pullrequestfactory.domain

data class PullRequest(val title: String, val base: Branch, val head: Branch) {

    fun is_pull_request(): Boolean = base.iteration_nr() < head.iteration_nr()

    fun is_base_master(): Boolean = base.name == "master"

}
