package pullrequestfactory.domain

class PullRequestTitleFactory {

    fun create_pull_request_titles(branches: List<Branch>, candidate: Candidate): List<String> {
        return branches.mapIndexed { idx, br ->
            val (_, _, _, iterationNr, pairingPartner) = br.name.split("_")
            val sessionNr = when {
                idx != 0 && branches[idx.dec()].name.endsWith(pairingPartner) -> idx
                else -> idx.inc()
            }
            "${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session $sessionNr ${pairingPartner.capitalize()}"
        }
    }

}
