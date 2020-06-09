package pullrequestfactory.domain

class PullRequests {

    fun create_pull_requests_for(branches: List<Branch>): List<PullRequest> {
        val titles = BranchTitles(branches).create()
        val baseBranches = BaseBranches(branches).create()
        val pullRequests = branches.mapIndexed { idx, branch ->
            PullRequest(
                    title = titles[idx],
                    base = baseBranches[idx],
                    head = branch)
        }
        return mark_pull_request_titles_with_pr(pullRequests.toMutableList())
    }

    private fun mark_pull_request_titles_with_pr(pullRequests: MutableList<PullRequest>): List<PullRequest> {
        for (i in pullRequests.indices) {
            if (pullRequests[i].is_base_master()) {
                continue
            }
            when {
                pullRequests[i].is_pull_request() -> pullRequests[i - 1] = PullRequest(
                        pullRequests[i - 1].title + " [PR]",
                        pullRequests[i - 1].base,
                        pullRequests[i - 1].head)
            }
        }
        return pullRequests
    }

}
