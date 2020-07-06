package pullrequestfactory.domain.pullrequests

interface GithubPullRequestsReadRepo {

    fun get_all_open_pull_requests(): List<GetPullRequest>

}
