package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.GithubWriteRepo
import pullrequestfactory.domain.PatchPullRequest
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.domain.UI

class GithubHttpWriteRepo(
        private val repoPath: String,
        private val basicAuthToken: String,
        private val ui: UI) : GithubWriteRepo {

    override fun create_pull_request(pullRequest: PullRequest) {
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
