package pullrequestfactory.io.repositories

import khttp.responses.Response
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesRepo

class GithubHttpBranchesRepo(
        private val repoUrl: String,
        private val response: Response,
        private val httpClient: HttpClient) : GithubBranchesRepo {

    override fun get_all_branches(): List<Branch> {
        return GithubHttpReadRepo(httpClient).get_list(response, "$repoUrl/branches")
    }

}
