package pullrequestfactory.domain.branches

interface GithubBranchesRepo {

    fun getBranches(): List<Branch>

}
