package pullrequestfactory.io.repositories

import khttp.responses.Response
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo
import pullrequestfactory.domain.uis.UI

class GithubHttpPullRequestsReadRepo(
        private val repoUrl: String,
        private val response: Response,
        private val httpClient: HttpClient,
        private val ui: UI
) : GithubPullRequestsReadRepo {

    override fun getAllOpenPullRequests(): List<GetPullRequest> {
        return GithubHttpReadRepo(httpClient, ui).getList(response, "$repoUrl/pulls")
    }

}
