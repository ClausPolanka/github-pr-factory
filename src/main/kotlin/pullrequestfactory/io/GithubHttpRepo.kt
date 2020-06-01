package pullrequestfactory.io

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.*

class GithubHttpRepo(
        private val baseUrli: String,
        private val repoName: String,
        private val basicAuthToken: String,
        private val cacheRepo: CacheRepo,
        private val ui: UI) : GithubReadRepo, GithubWriteRepo {

    override fun get_all_branches(): List<Branch> {
        val response = khttp.get("$baseUrli/repos/ClausPolanka/$repoName/branches?page=1")
        if (response.statusCode == 403) {
            ui.show("Too many requests to Github within time limit")
            return emptyList()
        }
        cacheRepo.cache(response.text, pageNr = 1)
        return get_all_branches_for(response)
    }

    private fun get_all_branches_for(response: Response): List<Branch> {
        val lastPage = KHttpResponseLastPageParser().last_page_of_branches(response)
        val allBranches = mutableListOf<List<Branch>>()
        allBranches.add(Klaxon().parseArray(response.text)!!)
        (2..lastPage.toInt()).forEach {
            val json = khttp.get("$baseUrli/repos/ClausPolanka/$repoName/branches?page=$it").text
            cacheRepo.cache(response.text, pageNr = it)
            allBranches.add(Klaxon().parseArray(json)!!)
        }
        return allBranches.flatten()
    }

    override fun create_pull_request(pullRequest: PullRequest) {
        ui.show("Create pull request on Github: $pullRequest")
        val response = khttp.post(
                url = "$baseUrli/repos/ClausPolanka/$repoName/pulls",
                headers = mapOf(
                        "Accept" to "application/json",
                        "Authorization" to "Basic $basicAuthToken",
                        "Content-Type" to "application/json"),
                data = Klaxon().toJsonString(pullRequest))
        ui.show(response.toString())
    }
}
