package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.RateLimit
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.KhttpClientStats

class RateLimitCheckedProgram(
        private val ui: UI,
        private val githubApiClient: GithubAPIClient,
        private val httpClientStats: KhttpClientStats,
        private val delegate: Program,
        private val requiredNrOfRequests: Int
) : Program {

    override fun execute() {
        val rateLimitBefore = githubApiClient.get_rate_limit()
        ui.show(rateLimitBefore)
        when {
            rateLimitBefore.isExeeded(requiredNrOfRequests) -> {
                ui.showRateLimitExeeded(rateLimitBefore)
                ui.showRateLimitAfter()
            }
            else -> {
                delegate.execute()
                ui.showRateLimitAfter()
            }
        }
    }

    private fun UI.showRateLimitExeeded(rateLimitBefore: RateLimit) {
        show("The limit exeeded for calling the Github API with your Github user")
        show("Please retry at: ${rateLimitBefore.localResetDateTime()}")
    }

    private fun UI.show(rateLimitBefore: RateLimit) {
        show(System.lineSeparator())
        show("Before: $rateLimitBefore")
    }

    private fun UI.showRateLimitAfter() {
        show(System.lineSeparator())
        show(httpClientStats.stats())
        show(System.lineSeparator())
        val rateLimitAfter = githubApiClient.get_rate_limit()
        show("After: $rateLimitAfter")
    }

}
