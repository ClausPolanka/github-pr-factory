package pullrequestfactory.domain

class GithubPRFactory(private val githubReadRepo: GithubReadRepo, private val githubWriteRepo: GithubWriteRepo) {

    fun create_pull_requests(candidate: Candidate, orderByPairingPartner: List<String>) {
        val branches = get_branches_for(candidate)
        val sortedBranches = BranchSorter().sort_branches(branches, orderByPairingPartner)
        val pullRequests = PullRequests().create_pull_requests(sortedBranches, candidate)
        pullRequests.forEach { githubWriteRepo.create_pull_request(it) }
    }

    private fun get_branches_for(candidate: Candidate): List<Branch> {
        return githubReadRepo.get_all_branches()
                .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.name.contains(candidate.lastName, ignoreCase = true) }
    }

}
