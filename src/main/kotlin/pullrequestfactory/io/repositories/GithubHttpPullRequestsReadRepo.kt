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
        var pullRequests = emptyList<GetPullRequest>()
        pages.forEach {
            val json = get("$repoUrl/pulls?page=$it").text
            pullRequests = pullRequests + toGetPullRequests(json)
        }
        return pullRequests
    }

    private fun toGetPullRequests(json: String) =
            (Klaxon().parseArray(json) ?: emptyList<GetPullRequest>())

}
