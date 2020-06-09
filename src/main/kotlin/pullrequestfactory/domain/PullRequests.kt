package pullrequestfactory.domain

class PullRequests(private val branches: List<Branch>) {

    fun create_pull_requests_for(candidate: Candidate): List<PullRequest> {
        val sessions = Sessions(branches).create()
        val baseBranches = BaseBranches(branches).create()
        val headBranches = HeadBranches(branches).create()
        val pullRequests = branches.mapIndexed { currentBrIdx, currentBranch ->
            val (_, _, _, iterationNr, pairingPartner) = currentBranch.parts()
            PullRequest(
                    title = title_for(candidate, iterationNr, sessions[currentBrIdx].toInt(), pairingPartner),
                    base = baseBranches[currentBrIdx],
                    head = headBranches[currentBrIdx])
        }
        return mark_pull_requests_with_pr(pullRequests.toMutableList())
    }

    private fun title_for(
            candidate: Candidate,
            iterationNr: String,
            newSessionNr: Int,
            pairingPartner: String): String {
        return "${candidate.firstName.capitalize()} ${candidate.lastName.capitalize()} " +
                "Iteration $iterationNr / Session $newSessionNr ${pairingPartner.capitalize()}"
    }

    private fun base_branch(currentBrIdx: Int): String {
        return when (currentBrIdx) {
            0 -> "master"
            else -> branches[currentBrIdx.dec()].name
        }
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





