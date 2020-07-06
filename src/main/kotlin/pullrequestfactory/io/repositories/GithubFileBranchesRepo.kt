package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.pullrequests.GithubPullRequestsReadRepo
import java.io.File

class GithubFileBranchesRepo : GithubBranchesRepo, GithubPullRequestsReadRepo {

    override fun get_all_branches(): List<Branch> {
        val allBranches = mutableListOf<List<Branch>>()
        (1..9).forEach {
            val json = File("json/branches-page-$it.json").readText()
            val branches = Klaxon().parseArray<Branch>(json)
            allBranches.add(branches!!)
        }
        return allBranches.flatten()
    }

    override fun get_all_open_pull_requests(): List<GetPullRequest> {
        val pullRequests = mutableListOf<List<GetPullRequest>>()
        (1..2).forEach {
            val json = File("json/prs-page-$it.json").readText()
            val prs = Klaxon().parseArray<GetPullRequest>(json)
            pullRequests.add(prs!!)
        }
        return pullRequests.flatten()
    }

}
