package pullrequestfactory.domain

class EmptyGithubBranchesRepo : GithubBranchesRepo {

    override fun get_all_branches(): List<Branch> {
        return emptyList()
    }

}
