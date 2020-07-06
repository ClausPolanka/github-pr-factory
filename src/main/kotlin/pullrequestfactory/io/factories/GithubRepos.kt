package pullrequestfactory.io.factories

import pullrequestfactory.domain.*
import pullrequestfactory.io.GithubHttpBranchesRepo
import pullrequestfactory.io.GithubHttpPullRequestsRepo

class GithubRepos(private val repoUrl: String, private val ui: UI) : GithubReadRepo {

    override fun get_all_branches(): List<Branch> {
        return create_branch_repo().get_all_branches()
    }

    private fun create_branch_repo(): GithubBranchesRepo {
        val response = khttp.get("$repoUrl/branches?page=1")
        if (response.statusCode == 403) {
            ui.show("Too many requests to Github within time limit")
            return EmptyGithubBranchesRepo()
        }
        return GithubHttpBranchesRepo(repoUrl, response, NoopCache())
    }

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return create_pull_requests_repo().get_all_open_pull_requests()
    }

    private fun create_pull_requests_repo(): GithubPullRequestsRepo {
        val response = khttp.get("$repoUrl/pulls?page=1")
        if (response.statusCode == 403) {
            ui.show("Too many requests to Github within time limit")
            return EmptyPullRequestsRepo()
        }
        return GithubHttpPullRequestsRepo(repoUrl, response, ui, NoopCache())
    }

}
