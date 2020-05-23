package pullrequestfactory.io

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.GithubRepo
import pullrequestfactory.domain.PullRequest
import java.io.File

class GithubFileRepo : GithubRepo {

    override fun get_all_branches(): List<Branch> {
        val allBranches = mutableListOf<List<Branch>>()
        (1..9).forEach {
            val json = File("json/branches-page-$it.json").readText()
            val branches = Klaxon().parseArray<Branch>(json)
            allBranches.add(branches!!)
        }
        return allBranches.flatten()
    }

    override fun create_pull_request(pullRequest: PullRequest) {
        println("Create pull request on Github: $pullRequest")
        println(Klaxon().toJsonString(pullRequest))
    }
}
