package pullrequestfactory.io.repositories

import khttp.responses.Response
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo

class GithubHttpPullRequestsReadRepo(
        private val repoUrl: String,
        private val response: Response,
        private val basicAuthToken: String) : GithubPullRequestsReadRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return GithubHttpReadRepo().get_list(response, "$repoUrl/pulls", basicAuthToken)
    }

}
