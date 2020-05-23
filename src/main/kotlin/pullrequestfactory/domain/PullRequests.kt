package pullrequestfactory.domain

class PullRequests {

    fun create_pull_requests(branches: List<Branch>, candidate: Candidate): List<PullRequest> {
        return branches.mapIndexed { idx, br ->
            val (_, _, _, iterationNr, pairingPartner) = br.name.split("_")
            val sessionNr = when {
                idx != 0 && branches[idx.dec()].name.endsWith(pairingPartner) -> idx
                else -> idx.inc()
            }
            PullRequest("${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session $sessionNr ${pairingPartner.capitalize()}")
        }
    }

}
