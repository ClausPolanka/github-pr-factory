package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.KhttpClientStats

class RateLimitCheckedPrograms(
        private val ui: UI,
        private val githubApiClient: GithubAPIClient,
        private val httpClientStats: KhttpClientStats,
        private val delegate: Program,
        private val requiredNrOfRequests: Int
) {
    fun instance(debug: Boolean): Program {
        return when (debug) {
            true -> DebugRateLimitCheckedProgram(
                    ui,
                    githubApiClient,
                    httpClientStats,
                    delegate,
                    requiredNrOfRequests
            )
            else -> RateLimitCheckedProgram(
                    ui,
                    githubApiClient,
                    delegate,
                    requiredNrOfRequests
            )
        }
    }
}
