package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.uis.ConsoleUI

class OpenPRProgramLastSessionFinished(
    private val ui: UI,
    private val gitHubApiClient: GithubAPIClient,
    private val candidate: Candidate,
    private val pairingPartner: List<PairingPartner>
) : OpenPRProgram {

    override fun execute() {
        val f = GithubPRFactory(
            ConsoleUI(),
            gitHubApiClient,
            gitHubApiClient,
            BranchSyntaxValidator(ui),
            PullRequestLastFinishedMarker()
        )
        f.openPullRequests(candidate, pairingPartner)
    }

}
