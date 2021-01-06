package pullrequestfactory.domain

import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.branches.Branches
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.GithubPullRequestsRepo
import pullrequestfactory.domain.pullrequests.PullRequestMarker
import pullrequestfactory.domain.uis.UI

class GithubPRFactory(
        val ui: UI,
        private val githubBranchesRepo: GithubBranchesRepo,
        private val githubPullRequestsRepo: GithubPullRequestsRepo,
        private val branchSyntaxValidator: BranchSyntaxValidator,
        private val pullRequestMarker: PullRequestMarker
) {

    /**
     * @param pairingPartner A list of George backend chapter team member names which must be in the order in
     * which they participated in the candidate's second round interview. This is required since Github returns
     * branches unsorted. The names in the list must be the same (case insensitve) as the ones which are at the end of
     * each branch.
     */
    fun open_pull_requests(candidate: Candidate, pairingPartner: List<PairingPartner>) {
        val branches = get_branches_for(candidate)
        val prs = branches.get_pull_requests_for(pairingPartner)
        prs.forEach { githubPullRequestsRepo.open_pull_request(it) }
    }

    private fun get_branches_for(candidate: Candidate): Branches {
        val branches = githubBranchesRepo.get_all_branches()
                .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.name.contains(candidate.lastName, ignoreCase = true) }
                .map {
                    branchSyntaxValidator.validate(it)
                    it
                }
        return Branches(branches, pullRequestMarker)
    }

    fun close_pull_requests_for(candidate: Candidate) {
        ui.show("Closing pull requests for: $candidate")
        val prs = githubPullRequestsRepo.get_all_open_pull_requests()
                .filter { it.title.contains(candidate.firstName, ignoreCase = true) }
                .filter { it.title.contains(candidate.lastName, ignoreCase = true) }

        prs.forEach { githubPullRequestsRepo.close_pull_request(it.number) }
        ui.show("Successfully closed all pull requests for: $candidate")
        ui.show("Have a nice day. Bye bye.")
    }

}
