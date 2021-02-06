package pullrequestfactory.io.repositories

import pullrequestfactory.domain.pullrequests.EmptyPullRequestsReadRepo
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo
import pullrequestfactory.domain.uis.UI

class GithubHttpPullRequestsReadRepos(
    private val repoPath: String,
    private val httpClient: HttpClient,
    private val ui: UI
) : GithubPullRequestsReadRepo {

    override fun getAllOpenPullRequests(): List<GetPullRequest> {
        return createPullRequestsRepo().getAllOpenPullRequests()
    }

    private fun createPullRequestsRepo(): GithubPullRequestsReadRepo {
        val url = "$repoPath/pulls?page=1"
        val response = httpClient.get(url)
        return when (response.statusCode) {
            403 -> {
                ui.show("Too many requests to Github within time limit")
                EmptyPullRequestsReadRepo()
            }
            404 -> {
                ui.show("Couldn't find following URL: $url")
                EmptyPullRequestsReadRepo()
            }
            else -> GithubHttpPullRequestsReadRepo(repoPath, response, httpClient, ui)
        }
    }
}
