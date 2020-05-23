package pullrequestfactory.domain

class GithubPRFactory(private val githubRepo: GithubRepo) {
    fun create_pull_requests_for_candidate(candidate: Candidate) {
        val branches = githubRepo.get_all_branches()
                .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.name.contains(candidate.lastName, ignoreCase = true) }
        branches.forEachIndexed { idx, br ->
            val (_, _, _, iterationNr, pairingPartner) = br.name.split("_")
            val sessionNr = when {
                idx != 0 && branches[idx.dec()].name.endsWith(pairingPartner) -> idx
                else -> idx.inc()
            }
            githubRepo.create_pull_request(
                    "${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session $sessionNr ${pairingPartner.capitalize()}")
        }
    }
}
