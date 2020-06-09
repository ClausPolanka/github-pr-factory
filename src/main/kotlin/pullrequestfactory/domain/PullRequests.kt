package pullrequestfactory.domain

class PullRequests(private val branches: List<Branch>) {

    fun create_pull_requests_for(candidate: Candidate): List<PullRequest> {
        val titles = BranchTitles(branches).create()
        val baseBranches = BaseBranches(branches).create()
        val headBranches = HeadBranches(branches).create()
        val pullRequests = branches.mapIndexed { idx, _ ->
            PullRequest(
                    title = titles[idx],
                    base = baseBranches[idx],
                    head = headBranches[idx])
        }
        return mark_pull_requests_with_pr(pullRequests.toMutableList())
    }

    private fun mark_pull_requests_with_pr(pullRequests: MutableList<PullRequest>): List<PullRequest> {
        for (i in pullRequests.indices) {
            if (pullRequests[i].base == "master") {
                continue
            }
            val currBaseIterNr = pullRequests[i].base.split("_").dropLast(1).last().toInt()
            val currHeadIterNr = pullRequests[i].head.split("_").dropLast(1).last().toInt()

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





