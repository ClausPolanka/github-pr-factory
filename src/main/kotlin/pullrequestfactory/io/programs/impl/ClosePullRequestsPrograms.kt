package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats

class ClosePullRequestsPrograms(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        baseUrl: String,
        private val repoUrl: String,
        authToken: String
) : Program {

    private val requiredNrOfRequestsForClosingPRs = 15
    private val httpClient = KhttpClient(authToken)
    private val httpClientStats = KhttpClientStats(httpClient)
    private val githubApiClient = GithubAPIClient(httpClient, baseUrl)

    override fun execute() {
        RateLimitCheckedProgram(githubApiClient,
                httpClientStats,
                create(),
                requiredNrOfRequestsForClosingPRs).execute()
    }

    private fun create(): ClosePRProgram {
        return ClosePullRequestProgram(
                ui,
                programArgs,
                repoUrl,
                httpClientStats
        )
    }

}
