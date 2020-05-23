package pullrequestfactory.domain

class GithubPRFactory(private val githubRepo: GithubRepo, private val candidate: Candidate) {

    fun create_pull_requests() {
        val branches = get_branches()
        create_pull_requests(branches)
    }

    private fun get_branches(): List<Branch> {
        return githubRepo.get_all_branches()
                .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.name.contains(candidate.lastName, ignoreCase = true) }
    }

    private fun create_pull_requests(branches: List<Branch>) {
        branches.forEachIndexed { idx, br ->
            val (_, _, _, iterationNr, pairingPartner) = br.name.split("_")
            val sessionNr = when {
                idx != 0 && branches[idx.dec()].name.endsWith(pairingPartner) -> idx
                else -> idx.inc()
            }
            val prTitle = "${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session $sessionNr ${pairingPartner.capitalize()}"
            githubRepo.create_pull_request(prTitle)
        }
    }
}
