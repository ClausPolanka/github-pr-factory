package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsRepo
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.UI

class GithubHttpPullRequestsRepo(
        private val repoPath: String,
        private val httpClient: HttpClient,
        private val ui: UI
) : GithubPullRequestsRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return GithubHttpPullRequestsReadRepos(repoPath, httpClient, ui).get_all_open_pull_requests()
    }

    override fun openPullRequest(pullRequest: PullRequest) {
        ui.show("Open pull request on Github: $pullRequest")
        val url = "$repoPath/pulls"
        val response = httpClient.post(
                url = url,
                data = Klaxon().toJsonString(pullRequest))
        handle(response, url)
    }

    override fun closePullRequest(number: Int) {
        ui.show("Close pull request with number: '$number'")
        val url = "$repoPath/pulls/$number"
        val response = httpClient.patch(
                url = url,
                data = Klaxon().toJsonString(object {
                    val state = "closed"
                }))
        handle(response, url)
    }

    private fun handle(response: Response, url: String) {
        when (response.statusCode) {
            403 -> ui.show("Too many requests to Github within time limit")
            404 -> ui.show("Couldn't find following URL: $url")
            else -> ui.show(response.toString())
        }
    }

}
