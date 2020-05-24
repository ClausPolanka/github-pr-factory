package pullrequestfactory.domain

class PullRequests {

    fun create_pull_requests(branches: List<Branch>, candidate: Candidate): List<PullRequest> {
        var sessionNumber = 0
        return branches.mapIndexed { currentBrIdx, currentBranch ->
            val (_, _, _, iterationNr, pairingPartner) = currentBranch.name.split("_")
            val base = when (currentBrIdx) {
                0 -> "master"
                else -> branches[currentBrIdx.dec()].name
            }
            val sessionNr = when {
                // same session because previous branch is from same pairing partner
                currentBrIdx != 0 &&
                        branches[currentBrIdx.dec()].name.endsWith(pairingPartner) -> sessionNumber

                // current branch idx is higher than session number because previous session switched iterations but
                // stayed in same session
                currentBrIdx != 0
                        && (!branches[currentBrIdx.dec()].name.endsWith(pairingPartner)
                        && currentBrIdx > sessionNumber) -> sessionNumber.inc()

                else -> sessionNumber.inc()
            }
            sessionNumber = sessionNr
            PullRequest(
                    title = "${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session $sessionNr ${pairingPartner.capitalize()}",
                    base = base,
                    head = currentBranch.name)
        }
    }

}
