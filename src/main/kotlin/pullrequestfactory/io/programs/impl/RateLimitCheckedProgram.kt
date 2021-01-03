package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program

class RateLimitCheckedProgram(
        private val githubApiClient: GithubAPIClient,
        private val delegate: OpenPRProgram,
        private val requiredNrOfRequests: Int) : Program {

    override fun execute() {
        val rateLimitBefore = githubApiClient.get_rate_limit()
        println()
        println("Rate rate limit before opening pull requests: $rateLimitBefore")
        when {
            rateLimitBefore.isExeeded(requiredNrOfRequests) -> {
                OpenPRProgramRateLimitExeeded(rateLimitBefore).execute()
            }
            else -> delegate.execute()
        }
    }

}
