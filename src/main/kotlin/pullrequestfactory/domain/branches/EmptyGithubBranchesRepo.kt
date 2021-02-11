package pullrequestfactory.domain.branches

class EmptyGithubBranchesRepo : GithubBranchesRepo {

    override fun getBranches(): List<Branch> {
        return emptyList()
    }

}
