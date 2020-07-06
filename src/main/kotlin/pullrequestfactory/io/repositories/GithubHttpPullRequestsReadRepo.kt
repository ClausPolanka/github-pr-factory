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
        val lastPage = HeaderLinkLastPageParser().last_page_of_branches(response.headers["link"])
        val allPullRequests = mutableListOf<List<GetPullRequest>>()
        allPullRequests.add(Klaxon().parseArray(response.text)!!)
        (2..lastPage.toInt()).forEach {
            val json = get("$repoUrl/pulls?page=$it").text
            allPullRequests.add(Klaxon().parseArray(json)!!)
        }
        return allPullRequests.flatten()
    }

}
