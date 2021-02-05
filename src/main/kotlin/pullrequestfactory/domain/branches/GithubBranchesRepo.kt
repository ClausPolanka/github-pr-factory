package pullrequestfactory.domain.branches

interface GithubBranchesRepo {

    fun getAllBranches(): List<Branch>

}
