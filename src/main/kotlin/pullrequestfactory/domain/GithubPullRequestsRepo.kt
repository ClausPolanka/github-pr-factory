package pullrequestfactory.domain

interface GithubPullRequestsRepo {

    fun get_all_open_pull_requests(): List<GetPullRequest>

}
