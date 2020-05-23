package pullrequestfactory.io

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.GithubRepo

class GithubHttpRepo(val repoName: String) : GithubRepo {
    override fun get_all_branches(): List<Branch> {
        val response = khttp.get("https://api.github.com/repos/ClausPolanka/$repoName/branches?page=1")
        val linkHeader = response.headers["link"]
        val lastPage = last_page(linkHeader!!)
        val allBranches = mutableListOf<List<Branch>>()
        allBranches.add(Klaxon().parseArray(response.text)!!)
        (2..lastPage.toInt()).forEach {
            val json = khttp.get("https://api.github.com/repos/ClausPolanka/$repoName/branches?page=$it").text
            allBranches.add(Klaxon().parseArray(json)!!)
        }
        return allBranches.flatten()
    }

    override fun create_pull_request(title: String) {
        TODO("not implemented")
    }
}

fun last_page(linkHeader: String): String {
    val pattern = "page=([0-9]+)".toRegex()
    val matches = pattern.findAll(linkHeader)
    return matches.last().groupValues[1]
}
