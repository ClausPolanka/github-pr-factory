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
    private val httpClient = KhttpClient(authToken)
    private val githubApiClient = GithubAPIClient(httpClient, baseUrl)

    override fun execute() {
        RateLimitCheckedProgram(githubApiClient,
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
                        httpClient,
                        authToken)
            }
            else -> OpenPRsProgramLastSessionNotFinished(ui,
                    programArgs,
                    baseUrl,
                    repoUrl,
                    httpClient,
                    authToken)
        }
    }

}

