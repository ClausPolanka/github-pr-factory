package pullrequestfactory.domain

interface GithubRepo {
    fun get_all_branches(): List<Branch>
    fun create_pull_request(pullRequest: PullRequest)
}
