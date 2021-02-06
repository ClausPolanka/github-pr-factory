package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.HttpClient
import pullrequestfactory.io.uis.ConsoleUI

class OpenPRsProgramLastSessionNotFinished(
    private val ui: UI,
    private val repoUrl: String,
    private val httpClient: HttpClient,
    private val candidate: Candidate,
    private val pairingPartner: List<PairingPartner>
) : OpenPRProgram {

    override fun execute() {
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClient)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, httpClient, ui)
        val f = GithubPRFactory(
            ConsoleUI(),
            branchesRepo,
            prRepo,
            BranchSyntaxValidator(ui),
            PullRequestLastNotFinishedMarker()
        )
        f.openPullRequests(candidate, pairingPartner)
    }

}
