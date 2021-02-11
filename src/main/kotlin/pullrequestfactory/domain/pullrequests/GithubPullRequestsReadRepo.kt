package pullrequestfactory.domain.pullrequests

interface GithubPullRequestsReadRepo {

    fun getPullRequests(): List<GetPullRequest>

}
