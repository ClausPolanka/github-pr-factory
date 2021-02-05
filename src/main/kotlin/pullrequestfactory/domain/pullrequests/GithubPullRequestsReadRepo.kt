package pullrequestfactory.domain.pullrequests

interface GithubPullRequestsReadRepo {

    fun getAllOpenPullRequests(): List<GetPullRequest>

}
