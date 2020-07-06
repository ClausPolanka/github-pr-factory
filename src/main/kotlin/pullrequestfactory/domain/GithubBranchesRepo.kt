package pullrequestfactory.domain

interface GithubBranchesRepo {

    fun get_all_branches(): List<Branch>

}
