package pullrequestfactory.domain

class PullRequests(val branches: List<Branch>) {

    fun create_pull_requests_for(candidate: Candidate): List<PullRequest> {
        var sessionNr = 0
        return branches.mapIndexed { currentBrIdx, currentBranch ->
            val (_, _, _, iterationNr, pairingPartner) = currentBranch.name.split("_")
            val base = create_base_branch(currentBrIdx)
            val newSessionNr = create_session_number(currentBrIdx, pairingPartner, sessionNr)
            sessionNr = newSessionNr
            PullRequest(
                    title = "${candidate.firstName} ${candidate.lastName} " +
                            "Iteration $iterationNr / Session $newSessionNr ${pairingPartner.capitalize()}",
                    base = base,
                    head = currentBranch.name)
        }
    }

    private fun create_base_branch(currentBrIdx: Int): String {
        return when (currentBrIdx) {
            0 -> "master"
            else -> branches[currentBrIdx.dec()].name
        }
    }

    private fun create_session_number(currentBrIdx: Int, pairingPartner: String, sessionNumber: Int): Int {
        return when {
            // same session because previous branch is from same pairing partner
            currentBrIdx != 0 && branches[currentBrIdx.dec()].name.endsWith(pairingPartner) -> sessionNumber

            // current branch idx is higher than session number because previous session switched iterations but
            // stayed in same session
            currentBrIdx != 0
                    && (!branches[currentBrIdx.dec()].name.endsWith(pairingPartner)
                    && currentBrIdx > sessionNumber) -> sessionNumber.inc()

            else -> sessionNumber.inc()
        }
    }

}
