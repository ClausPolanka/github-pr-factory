package pullrequestfactory.domain

interface GithubReadRepo {

    fun get_all_branches(): List<Branch>

    fun get_all_pull_requests(): List<GetPullRequest>

}
