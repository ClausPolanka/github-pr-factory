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
        val lastPage = HeaderLinkLastPageParser().last_page_of_branches_in(response.headers["link"])
        var branches = toBranches(response.text)
        (2..lastPage.toInt()).forEach {
            val json = get("$repoUrl/branches?page=$it").text
            branches = branches + toBranches(json)
        }
        return branches
    }

    private fun toBranches(json: String) =
            (Klaxon().parseArray(json) ?: emptyList<Branch>())

}
