package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.KhttpClient

class OpenPullRequestsProgram(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val baseUrl: String,
        private val repoUrl: String,
        private val authToken: String
) : Program {

    private val requiredNrOfRequestsForOpeningPRs = 24

    override fun execute() {
        create().execute()
    }

    private fun create(): OpenPRProgram {
        val httpClient = KhttpClient(authToken)
        val rateLimitBefore = GithubAPIClient(httpClient, baseUrl).get_rate_limit()

        println()
        println("Rate rate limit before opening pull requests: $rateLimitBefore")

        return when {
            rateLimitBefore.rate.remaining < requiredNrOfRequestsForOpeningPRs -> {
                OpenPRProgramRateLimitExeeded(rateLimitBefore)
            }
            programArgs.has_open_command_with_optional_options() -> {
                OpenPRProgramLastSessionFinished(ui, programArgs, baseUrl, repoUrl, httpClient, authToken)
            }
            else -> OpenPRsProgramLastSessionNotFinished(ui, programArgs, baseUrl, repoUrl, httpClient, authToken)
        }
    }

}

