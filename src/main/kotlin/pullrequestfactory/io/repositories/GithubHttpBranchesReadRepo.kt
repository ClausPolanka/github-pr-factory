package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesReadRepo
import pullrequestfactory.domain.caches.Cache

class GithubHttpBranchesReadRepo(
        private val repoUrl: String,
        private val response: Response,
        private val cache: Cache) : GithubBranchesReadRepo {

    override fun get_all_branches(): List<Branch> {
        cache.cache(response.text, pageNr = 1)
        return get_all_branches_for(response)
    }

    private fun get_all_branches_for(response: Response): List<Branch> {
        val lastPage = HeaderLinkLastPageParser().last_page_of_branches(response.headers["link"])
        val allBranches = mutableListOf<List<Branch>>()
        allBranches.add(Klaxon().parseArray(response.text)!!)
        (2..lastPage.toInt()).forEach {
            val json = khttp.get("$repoUrl/branches?page=$it").text
            cache.cache(response.text, pageNr = it)
            allBranches.add(Klaxon().parseArray(json)!!)
        }
        return allBranches.flatten()
    }

}
