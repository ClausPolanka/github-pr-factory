package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.get
import khttp.responses.Response
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesRepo

class GithubHttpBranchesRepo(
        private val repoUrl: String,
        private val response: Response) : GithubBranchesRepo {

    override fun get_all_branches(): List<Branch> {
        val pages = HeaderLinkPageParser().parse_pages(response.headers["link"])
        val branches = mutableListOf<List<Branch>>()
        pages.forEach {
            val json = get("$repoUrl/branches?page=$it").text
            branches.add(toBranches(json))
        }
        return branches.flatten()
    }

    private fun toBranches(json: String) =
            (Klaxon().parseArray(json) ?: emptyList<Branch>())

}
