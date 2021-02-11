package pullrequestfactory.io.programs.impl

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsRepo
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.repositories.GithubHttpHeaderLinkPageParser
import pullrequestfactory.io.repositories.HttpClient
import java.time.Instant

class GithubAPIClient(
    private val httpClient: HttpClient,
    baseUrl: String,
    repoUrl: String,
    private val ui: UI
) : GithubBranchesRepo, GithubPullRequestsRepo {
    private val urlForGitHubRateLimit = "$baseUrl/rate_limit"
    private val urlForGitHubBranches = "$repoUrl/branches"
    private val urlForGitHubPullRequests = "$repoUrl/pulls"

    fun getRateLimit(): RateLimit {
        val response = httpClient.get(urlForGitHubRateLimit)
        return when (response.statusCode) {
            401, 403, 404 -> {
                ui.show("Response Code: '${response.statusCode}'")
                defaultRateLimit()
            }
            else -> jsonParser().parse(response.text) ?: defaultRateLimit()
        }
    }

    override fun getBranches(): List<Branch> {
        val response = httpClient.get(urlForGitHubBranches)
        return when (response.statusCode) {
            401, 403, 404 -> {
                ui.show("Response Code: '${response.statusCode}'")
                emptyList()
            }
            else -> getList(response, urlForGitHubBranches)
        }
    }

    override fun getPullRequests(): List<GetPullRequest> {
        val response = httpClient.get(urlForGitHubPullRequests)
        return when (response.statusCode) {
            401, 403, 404 -> {
                ui.show("Response Code: '${response.statusCode}'")
                emptyList()
            }
            else -> getList(response, urlForGitHubPullRequests)
        }
    }

    override fun openPullRequest(pullRequest: PullRequest) {
        httpClient.post(
            url = urlForGitHubPullRequests,
            data = jsonParser().toJsonString(pullRequest)
        )
    }

    override fun closePullRequest(number: Int) {
        val url = "$urlForGitHubPullRequests/$number"
        httpClient.patch(
            url = url,
            data = jsonParser().toJsonString(PullRequstPatch(state = "closed"))
        )
    }

    private inline fun <reified T> getList(res: Response, url: String): List<T> {
        val pages = GithubHttpHeaderLinkPageParser.parsePages(res.headers["link"])
        val branches = mutableListOf<List<T>>()
        pages.forEach {
            val pagedUrl = "$url?page=$it"
            val response = httpClient.get(pagedUrl)
            when (response.statusCode) {
                401, 403, 404 -> ui.show("Response Code: '${response.statusCode}'")
            }
            val json = response.text
            branches.add((jsonParser().parseArray(json) ?: emptyList()))
        }
        return branches.flatten()
    }

    private fun defaultRateLimit() =
        RateLimit((Rate(limit = 0, remaining = 0, Instant.now(), 0)))

    private fun jsonParser(): Klaxon =
        Klaxon().converter(EpochMilliInstantConverter())

}

data class PullRequstPatch(val state: String)
