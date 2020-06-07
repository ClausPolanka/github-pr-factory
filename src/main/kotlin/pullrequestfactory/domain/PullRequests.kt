package pullrequestfactory.domain

class PullRequests(private val branches: List<Branch>) {

    fun create_pull_requests_for(candidate: Candidate): List<PullRequest> {
        var sessionNr = 0
        val pullRequests = branches.mapIndexed { currentBrIdx, currentBranch ->
            val (_, _, _, iterationNr, pairingPartner) = currentBranch.name.split("_")
            val newSessionNr = create_session_number(currentBrIdx, pairingPartner, sessionNr)
            sessionNr = newSessionNr
            PullRequest(
                    title = title_for(candidate, iterationNr, newSessionNr, pairingPartner),
                    base = base_branch(currentBrIdx),
                    head = currentBranch.name)
        }
        return mark_pull_requests_with_pr(pullRequests.toMutableList())
    }

    private fun create_session_number(currentBrIdx: Int, pairingPartner: String, sessionNumber: Int): Int {
        return when {
            // same session because previous branch is from same pairing partner
            currentBrIdx != 0 && branches[currentBrIdx.dec()].name.endsWith(pairingPartner) -> sessionNumber

            // current branch idx is higher than session number because previous session switched iterations but
            // stayed in same session
            currentBrIdx != 0 && currentBrIdx > sessionNumber -> sessionNumber.inc()

            else -> sessionNumber.inc()
        }
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





