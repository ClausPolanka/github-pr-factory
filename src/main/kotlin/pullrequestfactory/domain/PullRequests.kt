package pullrequestfactory.domain

class PullRequests {

    fun create_pull_requests(branches: List<Branch>, candidate: Candidate): List<PullRequest> {
        var sessionNumber = 0
        return branches.mapIndexed { idx, br ->
            val (_, _, _, iterationNr, pairingPartner) = br.name.split("_")
            val base = when (idx) {
                0 -> "master"
                else -> branches[idx.dec()].name
            }
            val sessionNr = when {
                idx != 0 && branches[idx.dec()].name.endsWith(pairingPartner) -> sessionNumber // same session
                idx != 0 && (!branches[idx.dec()].name.endsWith(pairingPartner) && idx > sessionNumber) -> sessionNumber.inc() // previous session switched iteration
                else -> sessionNumber.inc()
            }
            sessionNumber = sessionNr
            PullRequest(
                    title = "${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session $sessionNr ${pairingPartner.capitalize()}",
                    base = base,
                    head = br.name)
        }
    }

}
