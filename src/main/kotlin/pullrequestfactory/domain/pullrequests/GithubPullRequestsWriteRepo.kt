package pullrequestfactory.domain.pullrequests

interface GithubPullRequestsWriteRepo {

    fun openPullRequest(pullRequest: PullRequest)

    fun closePullRequest(number: Int)

}
