package pullrequestfactory.domain.branches

class EmptyGithubBranchesRepo : GithubBranchesRepo {

    override fun get_all_branches(): List<Branch> {
        return emptyList()
    }

}
