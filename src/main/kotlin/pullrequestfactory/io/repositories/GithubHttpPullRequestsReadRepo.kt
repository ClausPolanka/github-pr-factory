package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo
import pullrequestfactory.domain.uis.UI

class GithubHttpPullRequestsReadRepo(
        private val repoPath: String,
        private val response: Response,
        private val ui: UI) : GithubPullRequestsReadRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return get_all_pull_requests_for(response)
    }

    private fun get_all_pull_requests_for(response: Response): List<GetPullRequest> {
        val lastPage = HeaderLinkLastPageParser().last_page_of_branches(response.headers["link"])
        val allPullRequests = mutableListOf<List<GetPullRequest>>()
        ui.show("\tNumber of open pull request pages: '$lastPage'")
        ui.show("\tResponse json: '${response.text}")
        allPullRequests.add(Klaxon().parseArray(response.text)!!)
        ui.show("\tPage 1 open pull requests: '$allPullRequests'")
        (2..lastPage.toInt()).forEach {
            val json = khttp.get("$repoPath/pulls?page=$it").text
            allPullRequests.add(Klaxon().parseArray(json)!!)
        }
        return allPullRequests.flatten()
    }

}
