package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.RateLimit
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.*
import pullrequestfactory.io.uis.ConsoleUI
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset.UTC
import java.time.format.DateTimeFormatter

class OpenPullRequestsProgramWithOptionalOptions(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String) : Program {

    private val requiredNrOfRequestsForOpeningPRs = 24

    override fun execute() {
        create().execute()
    }

    private fun create(): OpenPRProgram {
        val token = programArgs.get_github_basic_auth_token()
        val httpClient = KhttpClient(token)
        val rateLimitBefore = GithubAPIClient(httpClient).get_rate_limit()

        println()
        println("Rate rate limit before opening pull requests: $rateLimitBefore")

        if (rateLimitBefore.rate.remaining < requiredNrOfRequestsForOpeningPRs) {
            return OpenPRProgramRateLimitExeeded(rateLimitBefore)
        } else {
            return OpenPRProgramWithOptionalOption(ui, programArgs, repoUrl, httpClient, token)
        }
    }

}

class OpenPRProgramWithOptionalOption(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String,
        private val httpClient: HttpClient,
        private val token: String
) : OpenPRProgram {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val pp = programArgs.get_pairing_partner()
        val httpClientStats = KhttpClientStats(httpClient)
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClientStats)
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
        val rateLimitAfter = GithubAPIClient(httpClient).get_rate_limit()
        println("Rate rate limit after opening pull requests: $rateLimitAfter")
    }

}

class OpenPRProgramRateLimitExeeded(private val rateLimitBefore: RateLimit) : OpenPRProgram {

    override fun execute() {
        println("The limit exeeded for calling the Github API with your Github user")
        println("Please retry at: ${to_local_date_time(rateLimitBefore.rate.reset)}")
    }

    private fun to_local_date_time(resetDateTime: Instant): String {
        val local = LocalDateTime.ofInstant(resetDateTime, UTC)
        return local.format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
    }

}

interface OpenPRProgram : Program
