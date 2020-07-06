package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.pullrequests.*
import pullrequestfactory.domain.uis.UI

class GithubHttpPullRequestsRepo(
        private val repoPath: String,
        private val basicAuthToken: String,
        private val ui: UI) : GithubPullRequestsRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return create_pull_requests_repo().get_all_open_pull_requests()
    }

    private fun create_pull_requests_repo(): GithubPullRequestsReadRepo {
        val response = khttp.get("$repoPath/pulls?page=1")
        if (response.statusCode == 403) {
            ui.show("Too many requests to Github within time limit")
            return EmptyPullRequestsReadRepo()
        }
        return GithubHttpPullRequestsReadRepo(repoPath, response, ui)
    }

    override fun open_pull_request(pullRequest: PullRequest) {
        ui.show("Create pull request on Github: $pullRequest")
        val response = khttp.post(
                url = "$repoPath/pulls",
                headers = mapOf(
                        "Accept" to "application/json",
                        "Authorization" to "Basic $basicAuthToken",
                        "Content-Type" to "application/json"),
                data = Klaxon().toJsonString(pullRequest))
        ui.show(response.toString())
    }

    override fun close_pull_request(number: Int) {
        ui.show("Close pull request with number: '$number'")
        val response = khttp.patch(
                url = "$repoPath/pulls/$number",
                headers = mapOf(
                        "Accept" to "application/json",
                        "Authorization" to "Basic $basicAuthToken",
                        "Content-Type" to "application/json"),
                data = Klaxon().toJsonString(PatchPullRequest(state = "closed")))
        ui.show("$response")
    }

}
