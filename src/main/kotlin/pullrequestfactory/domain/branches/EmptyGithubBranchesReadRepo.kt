package pullrequestfactory.domain.branches

class EmptyGithubBranchesReadRepo : GithubBranchesReadRepo {

    override fun get_all_branches(): List<Branch> {
        return emptyList()
    }

}
