package pullrequestfactory.domain

class GithubPRFactory(private val githubRepo: GithubRepo) {

    fun create_pull_requests(candidate: Candidate) {
        val branches = get_branches_for(candidate)
        val sortedBranches = BranchSorter().sort_branches(branches, order = listOf("Claus", "Berni", "Dominik", "Christian", "Shubi", "Markus", "Mihai"))
        val pullRequests = PullRequests().create_pull_requests(sortedBranches, candidate)
        pullRequests.forEach { githubRepo.create_pull_request(it) }
    }

    private fun get_branches_for(candidate: Candidate): List<Branch> {
        return githubRepo.get_all_branches()
                .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.name.contains(candidate.lastName, ignoreCase = true) }
    }
}
