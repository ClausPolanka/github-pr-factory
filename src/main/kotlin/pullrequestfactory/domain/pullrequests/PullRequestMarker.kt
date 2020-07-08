package pullrequestfactory.domain.pullrequests

interface PullRequestMarker {

    fun mark(pullRequests: List<PullRequest>): List<PullRequest>

}
