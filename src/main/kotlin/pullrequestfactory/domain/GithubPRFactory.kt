package pullrequestfactory.domain

import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.branches.Branches
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.GithubPullRequestsRepo
import pullrequestfactory.domain.pullrequests.PullRequestMarker
import pullrequestfactory.domain.uis.UI

class GithubPRFactory(
    private val ui: UI,
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
    fun openPullRequests(candidate: Candidate, pairingPartner: List<PairingPartner>) {
        val branches = getBranchesFor(candidate)
        val prs = branches.getPullRequestsFor(pairingPartner)
        prs.forEach { githubPullRequestsRepo.openPullRequest(it) }
    }

    private fun getBranchesFor(candidate: Candidate): Branches {
        val branches = githubBranchesRepo.getAllBranches()
            .filter { it.name.contains(candidate.firstName, ignoreCase = true) }
            .filter { it.name.contains(candidate.lastName, ignoreCase = true) }
            .map {
                branchSyntaxValidator.validate(it)
                it
            }
        return Branches(branches, pullRequestMarker)
    }

    fun closePullRequestsFor(candidate: Candidate) {
        ui.show("Closing pull requests for: $candidate")
        val prs = githubPullRequestsRepo.getAllOpenPullRequests()
            .filter { it.title.contains(candidate.firstName, ignoreCase = true) }
            .filter { it.title.contains(candidate.lastName, ignoreCase = true) }

        prs.forEach { githubPullRequestsRepo.closePullRequest(it.number) }
        ui.show("Successfully closed all pull requests for: $candidate")
        ui.show("Have a nice day. Bye bye.")
    }

}
