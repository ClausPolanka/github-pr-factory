package pullrequestfactory.domain

data class PullRequest(val title: String, val base: Branch, val head: Branch) {

    fun base_iteration_nr(): Int = base.iteration_nr()

    fun head_iteration_nr(): Int = head.iteration_nr()

}
