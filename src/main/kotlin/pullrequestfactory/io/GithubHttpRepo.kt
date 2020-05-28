package pullrequestfactory.io

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.GithubReadRepo
import pullrequestfactory.domain.GithubWriteRepo
import pullrequestfactory.domain.PullRequest
import java.io.File

class GithubHttpRepo(
        private val baseUrl: String,
        private val repoName: String,
        private val basicAuthToken: String,
        private val branchesMustBeCached: Boolean) : GithubReadRepo, GithubWriteRepo {

    override fun get_all_branches(): List<Branch> {
        val response = khttp.get("$baseUrl/repos/ClausPolanka/$repoName/branches?page=1")
        if (response.statusCode == 403) { // too many requests to Github.com
            println("Too many requests to Github within time limit")
            return emptyList()
        }
        return get_all_branches_for(response)
    }

    private fun get_all_branches_for(response: Response): List<Branch> {
        val lastPage = last_page_of_branches(response)
        val allBranches = mutableListOf<List<Branch>>()
        if (branchesMustBeCached) {
            File("json/branches-page-1.json").writeText(response.text)
        }
        allBranches.add(Klaxon().parseArray(response.text)!!)
        (2..lastPage.toInt()).forEach {
            val json = khttp.get("$baseUrl/repos/ClausPolanka/$repoName/branches?page=$it").text
            if (branchesMustBeCached) {
                File("json/branches-page-$it.json").writeText(json)
            }
            allBranches.add(Klaxon().parseArray(json)!!)
        }
        return allBranches.flatten()
    }

    override fun create_pull_request(pullRequest: PullRequest) {
        println("Create pull request on Github: $pullRequest")
        val response = khttp.post(
                url = "$baseUrl/repos/ClausPolanka/$repoName/pulls",
                headers = mapOf(
                        "Accept" to "application/json",
                        "Authorization" to "Basic $basicAuthToken",
                        "Content-Type" to "application/json"),
                data = Klaxon().toJsonString(pullRequest))
        println(response)
    }

    private fun last_page_of_branches(response: Response): String {
        if (response.headers["link"] == null) {
            return "-1"
        } else {
            val pattern = "page=([0-9]+)".toRegex()
            val matches = pattern.findAll(response.headers["link"] ?: "")
            return matches.last().groupValues[1]
        }
    }

}
