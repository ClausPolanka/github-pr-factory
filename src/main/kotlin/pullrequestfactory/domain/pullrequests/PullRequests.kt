package pullrequestfactory.domain.pullrequests

import pullrequestfactory.domain.branches.BaseBranches
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.BranchTitles

class PullRequests(val pullRequestMarker: PullRequestMarker) {

    fun create_pull_requests_for(branches: List<Branch>): List<PullRequest> {
        val titles = BranchTitles(branches).create()
        val baseBranches = BaseBranches(branches).create()
        val pullRequests = branches.mapIndexed { idx, branch ->
            PullRequest(_title = titles[idx], _base = baseBranches[idx], _head = branch)
        }
        return mark_pull_request_titles(pullRequests)
    }

    private fun mark_pull_request_titles(pullRequests: List<PullRequest>): List<PullRequest> {
        return pullRequestMarker.mark(pullRequests)
    }

}
