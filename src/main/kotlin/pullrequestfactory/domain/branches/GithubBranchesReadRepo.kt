package pullrequestfactory.domain.branches

interface GithubBranchesReadRepo {

    fun get_all_branches(): List<Branch>

}
