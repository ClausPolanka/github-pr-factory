package pullrequestfactory.domain

class PullRequests {

    fun create_pull_requests(branches: List<Branch>, candidate: Candidate): List<PullRequest> {
        var sNr = 0
        return branches.mapIndexed { idx, br ->
            val (_, _, _, iterationNr, pairingPartner) = br.name.split("_")
            val base = when (idx) {
                0 -> "master"
                else -> branches[idx.dec()].name
            }
            val sessionNr = when {
                sNr != 0 && branches[idx.dec()].name.endsWith(pairingPartner) -> sNr // same session
                sNr != 0 && (!branches[idx.dec()].name.endsWith(pairingPartner) && idx > sNr) -> sNr.inc() // previous session switched iteration
                else -> sNr.inc()
            }
            sNr = sessionNr
            PullRequest(
                    "${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session $sessionNr ${pairingPartner.capitalize()}",
                    base,
                    br.name)
        }
    }

}
