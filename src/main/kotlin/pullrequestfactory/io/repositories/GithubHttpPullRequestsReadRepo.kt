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

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return GithubHttpReadRepo(httpClient, ui).get_list(response, "$repoUrl/pulls")
    }

}
