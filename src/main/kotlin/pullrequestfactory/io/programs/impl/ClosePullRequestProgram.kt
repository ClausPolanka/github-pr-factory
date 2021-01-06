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

class ClosePullRequestProgram(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String,
        private val httpClient: HttpClient
) : ClosePRProgram {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClient)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, ui, httpClient)
        val f = GithubPRFactory(
                ConsoleUI(),
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(candidate)
    }

}
