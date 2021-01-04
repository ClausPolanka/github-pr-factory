package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.RateLimit
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.KhttpClientStats

class RateLimitCheckedProgram(
        private val githubApiClient: GithubAPIClient,
        private val httpClientStats: KhttpClientStats,
        private val delegate: Program,
        private val requiredNrOfRequests: Int) : Program {

    override fun execute() {
        val rateLimitBefore = githubApiClient.get_rate_limit()
        print(rateLimitBefore)
        when {
            rateLimitBefore.isExeeded(requiredNrOfRequests) -> {
                ProgramRateLimitExeeded(rateLimitBefore).execute()
                printRateLimitAfter()
            }
            else -> {
                delegate.execute()
                printRateLimitAfter()
            }
        }
    }

    private fun print(rateLimitBefore: RateLimit) {
        println()
        println("Before: $rateLimitBefore")
    }

    private fun printRateLimitAfter() {
        println()
        println(httpClientStats.stats())
        println()
        val rateLimitAfter = githubApiClient.get_rate_limit()
        println()
        println("After: $rateLimitAfter")
    }

}
