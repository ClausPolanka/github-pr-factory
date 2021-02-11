package pullrequestfactory.io.programs.impl

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsRepo
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.io.repositories.GithubHttpHeaderLinkPageParser
import pullrequestfactory.io.repositories.HttpClient
import java.time.Instant

class GithubAPIClient(
    private val httpClient: HttpClient,
    baseUrl: String,
    repoUrl: String
) : GithubBranchesRepo, GithubPullRequestsRepo {
    private val urlForGitHubRateLimit = "$baseUrl/rate_limit"
    private val urlForGitHubBranches = "$repoUrl/branches"
    private val urlForGitHubPullRequests = "$repoUrl/pulls"

    fun getRateLimit(): RateLimit {
        val res = httpClient.get(urlForGitHubRateLimit)
        return when (res.statusCode) {
            401, 403, 404 -> defaultRateLimit()
            else -> (jsonParser().parse(res.text) ?: defaultRateLimit())
        }
    }

    override fun getBranches(): List<Branch> {
        val response = httpClient.get(urlForGitHubBranches)
        return when (response.statusCode) {
            403, 404 -> emptyList()
            else -> getList(response, urlForGitHubBranches)
        }
    }

    override fun getPullRequests(): List<GetPullRequest> {
        val response = httpClient.get(urlForGitHubPullRequests)
        return when (response.statusCode) {
            403, 404 -> emptyList()
            else -> getList(response, urlForGitHubPullRequests)
        }
    }

    override fun openPullRequest(pullRequest: PullRequest) {
        httpClient.post(
            url = urlForGitHubPullRequests,
            data = Klaxon().toJsonString(pullRequest)
        )
    }

    override fun closePullRequest(number: Int) {
        val url = "$urlForGitHubPullRequests/$number"
        httpClient.patch(
            url = url,
            data = Klaxon().toJsonString(PullRequstPatch(state = "closed"))
        )
    }

    private inline fun <reified T> getList(res: Response, url: String): List<T> {
        val pages = GithubHttpHeaderLinkPageParser.parsePages(res.headers["link"])
        val branches = mutableListOf<List<T>>()
        pages.forEach {
            val pagedUrl = "$url?page=$it"
            val response = httpClient.get(pagedUrl)
            val json = response.text
            branches.add((Klaxon().parseArray(json) ?: emptyList()))
        }
        return branches.flatten()
    }

    private fun defaultRateLimit() =
        RateLimit((Rate(limit = 0, remaining = 0, Instant.now(), 0)))

    private fun jsonParser(): Klaxon =
        Klaxon().converter(EpochMilliInstantConverter())

}

data class PullRequstPatch(val state: String)
