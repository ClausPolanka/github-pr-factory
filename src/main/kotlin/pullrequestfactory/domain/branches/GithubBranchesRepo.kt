package pullrequestfactory.domain.branches

interface GithubBranchesRepo {

    fun get_all_branches(): List<Branch>

}
