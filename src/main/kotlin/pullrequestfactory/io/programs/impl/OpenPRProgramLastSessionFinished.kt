package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.HttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class OpenPRProgramLastSessionFinished(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val baseUrl: String,
        private val repoUrl: String,
        private val httpClient: HttpClient,
        private val token: String
) : OpenPRProgram {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val pp = programArgs.get_pairing_partner()
        val httpClientStats = KhttpClientStats(httpClient)
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClientStats, token)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, token, ui, httpClientStats)
        val f = GithubPRFactory(
                ConsoleUI(),
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastFinishedMarker())
        f.open_pull_requests(candidate, pp)
        println()
        println(httpClientStats.stats())
        println()
        val rateLimitAfter = GithubAPIClient(httpClient, baseUrl).get_rate_limit()
        println("Rate rate limit after opening pull requests: $rateLimitAfter")
    }

}
