package pullrequestfactory.io.repositories

import khttp.responses.Response
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsRepo
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.impl.Rate
import pullrequestfactory.io.programs.impl.RateLimit
import java.time.Instant

class GithubAPIClient(
    private val httpClient: HttpClient,
    baseUrl: String,
    repoUrl: String,
    private val ui: UI,
    private val jsonSerializer: Json
) : GithubBranchesRepo, GithubPullRequestsRepo {
    private val urlForGitHubRateLimit = "$baseUrl/rate_limit"
    private val urlForGitHubBranches = "$repoUrl/branches"
    private val urlForGitHubPullRequests = "$repoUrl/pulls"

    fun getRateLimit(): RateLimit {
        val response = httpClient.get(urlForGitHubRateLimit)
        return when (response.statusCode) {
            401, 403, 404 -> {
                ui.show("Get Rate Limit Response Code: '${response.statusCode}'")
                ui.show("Response: ${response.text}")
                defaultRateLimit()
            }
            else -> jsonSerializer.decodeFromString(response.text) ?: defaultRateLimit()
        }
    }

    override fun getBranches(): List<Branch> {
        val response = httpClient.get(urlForGitHubBranches)
        return when (response.statusCode) {
            401, 403, 404 -> {
                ui.show("Get Branches Response Code: '${response.statusCode}'")
                ui.show("Response: ${response.text}")
                emptyList()
            }
            else -> getList(response, urlForGitHubBranches)
        }
    }

    override fun getPullRequests(): List<GetPullRequest> {
        val response = httpClient.get(urlForGitHubPullRequests)
        return when (response.statusCode) {
            401, 403, 404 -> {
                ui.show("Get Pull Requests Response Code: '${response.statusCode}'")
                ui.show("Response: ${response.text}")
                emptyList()
            }
            else -> getList(response, urlForGitHubPullRequests)
        }
    }

    override fun openPullRequest(pullRequest: PullRequest) {
        val dto = PullRequestDto(title = pullRequest.title, base = pullRequest.base.name, head = pullRequest.head.name)
        val json = jsonSerializer.encodeToString(dto)
        val response = httpClient.post(
            url = urlForGitHubPullRequests,
            data = json
        )
        handleResponse(response, json)
    }

    private fun handleResponse(response: Response, json: String) {
        ui.show("Response Code: '${response.statusCode}'")
        when (response.statusCode) {
            401, 403, 404, 422 -> {
                ui.show("There seems to be something wrong with following JSON: $json")
                ui.show("Response: ${response.text}")
            }
        }
    }

    override fun closePullRequest(number: Int) {
        val url = "$urlForGitHubPullRequests/$number"
        val json = jsonSerializer.encodeToString(PullRequstPatch(state = "closed"))
        val response = httpClient.patch(
            url = url,
            data = json
        )
        handleResponse(response, json)
    }

    private inline fun <reified T> getList(res: Response, url: String): List<T> {
        val pages = GithubHttpHeaderLinkPageParser.parsePages(res.headers["link"])
        val list = mutableListOf<List<T>>()
        pages.forEach {
            val pagedUrl = "$url?page=$it"
            val response = httpClient.get(pagedUrl)
            when (response.statusCode) {
                401, 403, 404, 422 -> {
                    ui.show("Get Pull Requests Response Code: '${response.statusCode}'")
                    ui.show("Response: ${response.text}")
                }
                else -> list.add((jsonSerializer.decodeFromString(response.text) ?: emptyList()))
            }
        }
        return list.flatten()
    }

    private fun defaultRateLimit() =
        RateLimit((Rate(limit = 0, remaining = 0, Instant.now(), 0)))

}

@Serializable
data class PullRequestDto(val title: String, val base: String, val head: String)

@Serializable
data class PullRequstPatch(val state: String)
