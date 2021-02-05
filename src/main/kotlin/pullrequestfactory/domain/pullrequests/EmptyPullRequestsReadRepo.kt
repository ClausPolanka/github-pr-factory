package pullrequestfactory.domain.pullrequests

class EmptyPullRequestsReadRepo : GithubPullRequestsReadRepo {

    override fun getAllOpenPullRequests(): List<GetPullRequest> {
        return emptyList()
    }

}
