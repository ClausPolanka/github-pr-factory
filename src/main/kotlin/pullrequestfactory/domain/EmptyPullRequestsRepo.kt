package pullrequestfactory.domain

class EmptyPullRequestsRepo : GithubPullRequestsRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return emptyList()
    }

}
