package pullrequestfactory.io.repositories

import khttp.responses.Response
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo

class GithubHttpPullRequestsReadRepo(
        private val repoUrl: String,
        private val response: Response) : GithubPullRequestsReadRepo {

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        return ReadHttpRepo().get_list(response, "$repoUrl/pulls")
    }

}
