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

class ClosePullRequestsProgram(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String) : Program {

    // TODO Add rate limit check
    override fun execute() {
        val candidate = programArgs.get_candidate()
        val token = programArgs.get_github_basic_auth_token()

        val httpClient = KhttpClient(token)
        val rateLimitBefore = GithubAPIClient(httpClient).get_rate_limit()
        println("Rate rate limit before closing pull requests: $rateLimitBefore")
        println()
        val httpClientStats = KhttpClientStats(httpClient)
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClientStats)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, token, ui, httpClientStats)
        val f = GithubPRFactory(
                ConsoleUI(),
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(candidate)
        println()
        println(httpClientStats.stats())
        val rateLimitAfter = GithubAPIClient(httpClient).get_rate_limit()
        println()
        println("Rate rate limit after closing pull requests: $rateLimitAfter")
    }

}
