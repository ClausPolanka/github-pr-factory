package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesReadRepo

class GithubHttpBranchesReadRepo(
        private val repoUrl: String,
        private val response: Response) : GithubBranchesReadRepo {

    override fun get_all_branches(): List<Branch> {
        return get_all_branches_for(response)
    }

    private fun get_all_branches_for(response: Response): List<Branch> {
        val lastPage = HeaderLinkLastPageParser().last_page_of_branches(response.headers["link"])
        val allBranches = mutableListOf<List<Branch>>()
        allBranches.add(Klaxon().parseArray(response.text)!!)
        (2..lastPage.toInt()).forEach {
            val json = khttp.get("$repoUrl/branches?page=$it").text
            allBranches.add(Klaxon().parseArray(json)!!)
        }
        return allBranches.flatten()
    }

}
