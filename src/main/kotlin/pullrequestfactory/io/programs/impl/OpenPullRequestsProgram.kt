package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats

class OpenPullRequestsProgram(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val baseUrl: String,
        private val repoUrl: String,
        private val authToken: String
) : Program {

    private val requiredNrOfRequestsForOpeningPRs = 30
    private val httpClient = KhttpClient(authToken)
    private val httpClientStats = KhttpClientStats(httpClient)
    private val githubApiClient = GithubAPIClient(httpClient, baseUrl)

    override fun execute() {
        RateLimitCheckedProgram(ui,
                githubApiClient,
                httpClientStats,
                create(),
                requiredNrOfRequestsForOpeningPRs).execute()
    }

    private fun create(): OpenPRProgram {
        return when {
            programArgs.has_open_command_with_optional_options() -> {
                OpenPRProgramLastSessionFinished(ui,
                        programArgs,
                        baseUrl,
                        repoUrl,
                        httpClientStats,
                        authToken)
            }
            else -> OpenPRsProgramLastSessionNotFinished(ui,
                    programArgs,
                    repoUrl,
                    httpClientStats,
                    authToken)
        }
    }

}

