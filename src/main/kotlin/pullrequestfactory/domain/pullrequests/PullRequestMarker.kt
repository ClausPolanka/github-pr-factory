package pullrequestfactory.domain.pullrequests

interface PullRequestMarker {

    fun markTitlesOf(pullRequests: List<PullRequest>): List<PullRequest>

}
