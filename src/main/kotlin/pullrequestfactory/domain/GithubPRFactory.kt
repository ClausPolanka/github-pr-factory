package pullrequestfactory.domain

class GithubPRFactory(private val githubRepo: GithubRepo) {
    fun create_pull_requests_for_candidate(candidate: Candidate) {
        val branches = githubRepo.get_all_branches()
        val branch = branches
                .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.name.contains(candidate.lastName, ignoreCase = true) }[0]
        val (_, _, _, iterationNr, pairingPartner) = branch.name.split("_")
        branches.forEachIndexed { idx, br ->
            githubRepo.create_pull_request(
                    "${candidate.firstName} ${candidate.lastName} Iteration $iterationNr / Session ${idx.inc()} ${pairingPartner.capitalize()}")
        }
    }
}
