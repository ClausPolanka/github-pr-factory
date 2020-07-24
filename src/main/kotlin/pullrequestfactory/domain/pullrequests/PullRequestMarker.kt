package pullrequestfactory.domain.pullrequests

interface PullRequestMarker {

    fun mark_titles_of(pullRequests: List<PullRequest>): List<PullRequest>

}
