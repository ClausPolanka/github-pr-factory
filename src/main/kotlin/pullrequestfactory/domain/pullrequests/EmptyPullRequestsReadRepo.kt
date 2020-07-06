package pullrequestfactory.domain.pullrequests

class EmptyPullRequestsReadRepo : GithubPullRequestsReadRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return emptyList()
    }

}
