package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class OpenPullRequestsProgram(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String) : Program {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val token = programArgs.get_github_basic_auth_token()
        val pp = programArgs.get_pairing_partner()

        val httpClient = KhttpClient()
        val rateLimit = GithubAPIClient(httpClient).get_rate_limit()
        val httpClientStats = KhttpClientStats(httpClient)
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClientStats)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, token, ui, httpClientStats)
        val f = GithubPRFactory(
                ConsoleUI(),
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.open_pull_requests(candidate, pp)
        println()
        println(httpClientStats.stats())
    }

}
