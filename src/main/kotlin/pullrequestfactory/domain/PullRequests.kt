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
            if (pullRequests[i].base.name == "master") {
                continue
            }
            val currBaseIterNr = pullRequests[i].base_iteration_nr()
            val currHeadIterNr = pullRequests[i].head_iteration_nr()

            when {
                currBaseIterNr < currHeadIterNr -> pullRequests[i - 1] = PullRequest(
                        pullRequests[i - 1].title + " [PR]",
                        pullRequests[i - 1].base,
                        pullRequests[i - 1].head)
            }
        }
        return pullRequests
    }

}





