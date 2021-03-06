package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.repositories.GithubAPIClient

class OpenPRsProgramLastSessionNotFinished(
    private val ui: UI,
    private val gitHubApiClient: GithubAPIClient,
    private val candidate: Candidate,
    private val pairingPartner: List<PairingPartner>
) : OpenPRProgram {

    override fun execute() {
        val f = GithubPRFactory(
            ui,
            gitHubApiClient,
            gitHubApiClient,
            PullRequestLastNotFinishedMarker()
        )
        f.openPullRequests(candidate, pairingPartner)
    }

}
