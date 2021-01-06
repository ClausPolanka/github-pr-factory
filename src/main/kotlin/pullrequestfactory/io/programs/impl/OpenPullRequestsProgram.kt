package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.KhttpClientStats

class OpenPullRequestsProgram(
        private val ui: UI,
        private val programArgs: ProgramArgs,
        private val repoUrl: String,
        private val githubAPIClient: GithubAPIClient,
        private val httpClientStats: KhttpClientStats
) : Program {

    private val requiredNrOfRequestsForOpeningPRs = 30

    override fun execute() {
        RateLimitCheckedPrograms(ui,
                githubAPIClient,
                httpClientStats,
                create(),
                requiredNrOfRequestsForOpeningPRs).instance(debug = true).execute()
    }

    private fun create(): OpenPRProgram {
        return when {
            programArgs.has_open_command_with_optional_options() -> {
                OpenPRProgramLastSessionFinished(ui,
                        programArgs,
                        repoUrl,
                        httpClientStats
                )
            }
            else -> OpenPRsProgramLastSessionNotFinished(ui,
                    programArgs,
                    repoUrl,
                    httpClientStats
            )
        }
    }

}

