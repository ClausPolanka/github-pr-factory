package pullrequestfactory.domain.branches

class EmptyGithubBranchesRepo : GithubBranchesRepo {

    override fun getAllBranches(): List<Branch> {
        return emptyList()
    }

}
