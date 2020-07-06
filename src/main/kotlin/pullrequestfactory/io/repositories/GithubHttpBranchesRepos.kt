package pullrequestfactory.io.repositories

import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.EmptyGithubBranchesReadRepo
import pullrequestfactory.domain.branches.GithubBranchesReadRepo
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.caches.NoopCache
import pullrequestfactory.domain.uis.UI

class GithubHttpBranchesRepos(private val repoUrl: String, private val ui: UI) : GithubBranchesRepo {

    override fun get_all_branches(): List<Branch> {
        return create_branch_repo().get_all_branches()
    }

    private fun create_branch_repo(): GithubBranchesReadRepo {
        val response = khttp.get("$repoUrl/branches?page=1")
        if (response.statusCode == 403) {
            ui.show("Too many requests to Github within time limit")
            return EmptyGithubBranchesReadRepo()
        }
        return GithubHttpBranchesReadRepo(repoUrl, response, NoopCache())
    }

}
