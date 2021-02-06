package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.HttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class OpenPRProgramLastSessionFinished(
    private val ui: UI,
    private val repoUrl: String,
    private val httpClient: HttpClient,
    private val candidate: Candidate,
    private val pairingPartner: List<PairingPartner>
) : OpenPRProgram {

    override fun execute() {
        val httpClientStats = KhttpClientStats(httpClient)
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClientStats)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, httpClientStats, ui)
        val f = GithubPRFactory(
            ConsoleUI(),
            branchesRepo,
            prRepo,
            BranchSyntaxValidator(ui),
            PullRequestLastFinishedMarker()
        )
        f.openPullRequests(candidate, pairingPartner)
    }

}
