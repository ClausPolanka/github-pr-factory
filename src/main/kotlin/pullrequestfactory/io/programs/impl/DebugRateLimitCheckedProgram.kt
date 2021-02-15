package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.GithubAPIClient
import pullrequestfactory.io.repositories.KhttpClientStats
import java.lang.System.lineSeparator

class DebugRateLimitCheckedProgram(
    private val ui: UI,
    private val githubApiClient: GithubAPIClient,
    private val httpClientStats: KhttpClientStats,
    private val delegate: Program,
    private val requiredNrOfRequests: Int
) : Program {

    override fun execute() {
        val rateLimitBefore = githubApiClient.getRateLimit()
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

    private fun UI.show(rateLimitBefore: RateLimit) {
        show("Before: $rateLimitBefore")
    }

    private fun UI.showRateLimitAfter() {
        show(lineSeparator())
        show(httpClientStats.stats())
        show(lineSeparator())
        val rateLimitAfter = githubApiClient.getRateLimit()
        show("After: $rateLimitAfter")
    }

}
