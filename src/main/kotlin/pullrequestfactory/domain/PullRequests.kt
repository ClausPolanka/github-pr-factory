package pullrequestfactory.domain

class PullRequests {

    fun create_pull_requests_for(branches: List<Branch>): List<PullRequest> {
        val titles = BranchTitles(branches).create()
        val baseBranches = BaseBranches(branches).create()
        val pullRequests = branches.mapIndexed { idx, branch ->
            PullRequest(
                    _title = titles[idx],
                    base = baseBranches[idx].name,
                    head = branch.name)
        }
        return mark_pull_request_titles_with_pr(pullRequests)
    }

    private fun mark_pull_request_titles_with_pr(pullRequests: List<PullRequest>): List<PullRequest> {
        pullRequests.forEachIndexed { idx, pr ->
            if (idx > 0)
                pullRequests[idx - 1].add_pr_mark_to_title(pr)
        }
        return pullRequests
    }

}
