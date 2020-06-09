package pullrequestfactory.domain

data class PullRequest(val title: String, val base: Branch, val head: Branch)
