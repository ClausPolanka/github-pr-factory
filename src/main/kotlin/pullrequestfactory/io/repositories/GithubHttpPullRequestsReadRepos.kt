package pullrequestfactory.io.repositories

import pullrequestfactory.domain.pullrequests.EmptyPullRequestsReadRepo
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo
import pullrequestfactory.domain.uis.UI

class GithubHttpPullRequestsReadRepos(
        private val repoPath: String,
        private val ui: UI,
        private val httpClient: KhttpClientStats) : GithubPullRequestsReadRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return create_pull_requests_repo().get_all_open_pull_requests()
    }

    private fun create_pull_requests_repo(): GithubPullRequestsReadRepo {
        val response = httpClient.get("$repoPath/pulls?page=1")
        return when (response.statusCode) {
            403 -> {
                ui.show("Too many requests to Github within time limit")
                EmptyPullRequestsReadRepo()
            }
            else -> GithubHttpPullRequestsReadRepo(repoPath, response, httpClient)
        }
    }
}
