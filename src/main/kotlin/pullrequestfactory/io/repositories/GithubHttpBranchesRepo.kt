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
        val allBranches = mutableListOf<List<Branch>>()
        allBranches.add(Klaxon().parseArray(response.text)!!)
        (2..lastPage.toInt()).forEach {
            val json = get("$repoUrl/branches?page=$it").text
            allBranches.add(Klaxon().parseArray(json)!!)
        }
        return allBranches.flatten()
    }

}
