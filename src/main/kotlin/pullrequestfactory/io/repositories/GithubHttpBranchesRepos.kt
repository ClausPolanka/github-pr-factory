package pullrequestfactory.io.repositories

import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.EmptyGithubBranchesRepo
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.uis.UI

class GithubHttpBranchesRepos(private val repoUrl: String,
                              private val ui: UI,
                              private val basicAuthToken: String) : GithubBranchesRepo {

    override fun get_all_branches(): List<Branch> {
        return create_branch_repo().get_all_branches()
    }

    private fun create_branch_repo(): GithubBranchesRepo {
        val response = khttp.get("$repoUrl/branches?page=1", headers = mapOf(
                "Accept" to "application/json",
                "Authorization" to "token $basicAuthToken",
                "Content-Type" to "application/json"))
        return when (response.statusCode) {
            403 -> {
                ui.show("Too many requests to Github within time limit")
                EmptyGithubBranchesRepo()
            }
            else -> GithubHttpBranchesRepo(repoUrl, response, basicAuthToken)
        }
    }

}
