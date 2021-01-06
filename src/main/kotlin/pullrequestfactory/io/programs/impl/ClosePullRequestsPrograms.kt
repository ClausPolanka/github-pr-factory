package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.KhttpClientStats

class ClosePullRequestsPrograms(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String,
        private val githubAPIClient: GithubAPIClient,
        private val httpClientStats: KhttpClientStats
) : Program {

    private val requiredNrOfRequestsForClosingPRs = 15

    override fun execute() {
        RateLimitCheckedPrograms(ui,
                githubAPIClient,
                httpClientStats,
                create(),
                requiredNrOfRequestsForClosingPRs).instance(debug = true).execute()
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
