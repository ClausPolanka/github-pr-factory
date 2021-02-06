package pullrequestfactory.io.repositories

import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.EmptyGithubBranchesRepo
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.uis.UI

class GithubHttpBranchesRepos(
    private val repoUrl: String,
    private val ui: UI,
    private val httpClient: HttpClient
) : GithubBranchesRepo {

    override fun getAllBranches(): List<Branch> {
        return createBranchRepo().getAllBranches()
    }

    private fun createBranchRepo(): GithubBranchesRepo {
        val url = "$repoUrl/branches?page=1"
        val response = httpClient.get(url)
        return when (response.statusCode) {
            403 -> {
                ui.show("Too many requests to Github within time limit")
                EmptyGithubBranchesRepo()
            }
            404 -> {
                ui.show("Couldn't find following URL: $url")
                EmptyGithubBranchesRepo()
            }
            else -> GithubHttpBranchesRepo(repoUrl, response, httpClient, ui)
        }
    }

}
