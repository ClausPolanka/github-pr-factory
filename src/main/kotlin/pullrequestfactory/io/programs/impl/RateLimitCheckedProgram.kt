package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.GithubAPIClient

class RateLimitCheckedProgram(
    private val ui: UI,
    private val githubApiClient: GithubAPIClient,
    private val delegate: Program,
    private val requiredNrOfRequests: Int
) : Program {

    override fun execute() {
        val rateLimitBefore = githubApiClient.getRateLimit()
        when {
            rateLimitBefore.isExeeded(requiredNrOfRequests) -> {
                ui.showRateLimitExeeded(rateLimitBefore)
            }
            else -> delegate.execute()
        }
    }

}

fun UI.showRateLimitExeeded(rateLimitBefore: RateLimit) {
    show("The limit exeeded for calling the Github API with your Github user")
    show("Please retry at: ${rateLimitBefore.localResetDateTime()}")
}
