package pullrequestfactory.domain.pullrequests

interface GithubPullRequestsWriteRepo {

    fun create_pull_request(pullRequest: PullRequest)

    fun close_pull_request(number: Int)

}
