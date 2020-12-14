package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsRepo
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.UI

class GithubHttpPullRequestsRepo(
        private val repoPath: String,
        private val authToken: String,
        private val ui: UI,
        private val httpClient: HttpClient) : GithubPullRequestsRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return GithubHttpPullRequestsReadRepos(repoPath, ui, httpClient, authToken).get_all_open_pull_requests()
    }

    override fun open_pull_request(pullRequest: PullRequest) {
        ui.show("Open pull request on Github: $pullRequest")
        val response = httpClient.post(
                url = "$repoPath/pulls",
                data = Klaxon().toJsonString(pullRequest))
        ui.show(response.toString())
    }

    override fun close_pull_request(number: Int) {
        ui.show("Close pull request with number: '$number'")
        val response = httpClient.patch(
                url = "$repoPath/pulls/$number",
                data = Klaxon().toJsonString(object {
                    val state = "closed"
                }))
        ui.show("$response")
    }

}
