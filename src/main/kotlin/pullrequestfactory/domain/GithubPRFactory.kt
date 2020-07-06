package pullrequestfactory.domain

class GithubPRFactory(
        private val githubReadRepo: GithubReadRepo,
        private val githubWriteRepo: GithubWriteRepo,
        private val branchSyntaxValidator: BranchSyntaxValidator) {

    /**
     * @param pairingPartner A list of George backend chapter team member names which must be in the order in
     * which they participated in the candidate's second round interview. This is required since Github returns
     * branches unsorted. The names in the list must be the same (case insensitve) as the ones which are at the end of
     * each branch.
     */
    fun open_pull_requests(candidate: Candidate, pairingPartner: List<String>) {
        val branches = get_branches_for(candidate)
        val sortedBranches = BranchSorter().sort_branches_by_pairing_partner(branches, pairingPartner)
        val pullRequests = PullRequests().create_pull_requests_for(sortedBranches)
        pullRequests.forEach { githubWriteRepo.create_pull_request(it) }
    }

    private fun get_branches_for(candidate: Candidate): List<Branch> {
        return githubReadRepo.get_all_branches()
                .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.name.contains(candidate.lastName, ignoreCase = true) }
                .map {
                    branchSyntaxValidator.validate(it)
                    it
                }
    }

    fun close_pull_requests_for(candidate: Candidate) {
        val prs = githubReadRepo.get_all_open_pull_requests()
                .filter { it.title.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.title.contains(candidate.lastName, ignoreCase = true) }

        prs.forEach { githubWriteRepo.close_pull_request(it.number) }
    }

}
