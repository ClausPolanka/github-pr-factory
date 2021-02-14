package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI

class ClosePullRequestProgram(
    private val ui: UI,
    private val gitHubApiClient: GithubAPIClient,
    private val candidate: Candidate
) : ClosePRProgram {

    override fun execute() {
        val f = GithubPRFactory(
            ui,
            gitHubApiClient,
            gitHubApiClient,
            BranchSyntaxValidator(ui),
            PullRequestLastNotFinishedMarker()
        )
        f.closePullRequestsFor(candidate)
    }

}
