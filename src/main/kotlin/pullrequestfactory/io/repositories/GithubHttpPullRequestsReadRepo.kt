package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.get
import khttp.responses.Response
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo

class GithubHttpPullRequestsReadRepo(
        private val repoUrl: String,
        private val response: Response) : GithubPullRequestsReadRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        val pages = HeaderLinkPageParser().parse_pages(response.headers["link"])
        val pullRequests = mutableListOf<List<GetPullRequest>>()
        pages.forEach {
            val json = get("$repoUrl/pulls?page=$it").text
            pullRequests.add(toGetPullRequests(json))
        }
        return pullRequests.flatten()
    }

    private fun toGetPullRequests(json: String) =
            (Klaxon().parseArray(json) ?: emptyList<GetPullRequest>())

}
