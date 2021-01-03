package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.HttpClient
import pullrequestfactory.io.uis.ConsoleUI

class OpenPRsProgramLastSessionNotFinished(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String,
        private val httpClient: HttpClient,
        private val token: String
) : OpenPRProgram {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val pp = programArgs.get_pairing_partner()
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClient, token)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, token, ui, httpClient)
        val f = GithubPRFactory(
                ConsoleUI(),
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.open_pull_requests(candidate, pp)
    }

}
