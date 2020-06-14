package pullrequestfactory.io

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.GetPullRequest
import pullrequestfactory.domain.GithubReadRepo
import java.io.File

class GithubFileReadRepo : GithubReadRepo {

    override fun get_all_branches(): List<Branch> {
        val allBranches = mutableListOf<List<Branch>>()
        (1..9).forEach {
            val json = File("json/branches-page-$it.json").readText()
            val branches = Klaxon().parseArray<Branch>(json)
            allBranches.add(branches!!)
        }
        return allBranches.flatten()
    }

    override fun get_all_pull_requests(): List<GetPullRequest> {
        TODO("not implemented")
    }

}
