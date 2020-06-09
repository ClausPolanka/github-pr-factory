package pullrequestfactory.domain

class PullRequests(private val branches: List<Branch>) {

    fun create_pull_requests_for(candidate: Candidate): List<PullRequest> {
        val titles = BranchTitles(branches).create()
        val baseBranches = BaseBranches(branches).create()
        val pullRequests = branches.mapIndexed { idx, branch ->
            PullRequest(
                    title = titles[idx],
                    base = baseBranches[idx],
                    head = branch)
        }
        return mark_pull_requests_with_pr(pullRequests.toMutableList())
    }

    private fun mark_pull_requests_with_pr(pullRequests: MutableList<PullRequest>): List<PullRequest> {
        for (i in pullRequests.indices) {
            if (pullRequests[i].base.name == "master") {
                continue
            }
            val currBaseIterNr = pullRequests[i].base.iteration_nr()
            val currHeadIterNr = pullRequests[i].head.iteration_nr()

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





