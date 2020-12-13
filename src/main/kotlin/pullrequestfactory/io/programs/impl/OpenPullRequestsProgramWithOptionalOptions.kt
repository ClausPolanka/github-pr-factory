package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.uis.ConsoleUI

class OpenPullRequestsProgramWithOptionalOptions(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String,
        private val basicAuthToken: String) : Program {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val token = programArgs.get_github_basic_auth_token()
        val pp = programArgs.get_pairing_partner()
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, basicAuthToken)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, token, ui)
        val f = GithubPRFactory(
                ConsoleUI(),
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastFinishedMarker())
        f.open_pull_requests(candidate, pp)
    }

}
