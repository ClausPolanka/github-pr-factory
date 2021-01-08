package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.pullrequests.PullRequestLastFinishedMarker
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs

class OpenPullRequestsProgramsInteractiveMode(
        private val ui: UI,
        private val pa: ProgramArgs,
        private val baseUrl: String,
        private val repoUrl: String,
        private val authToken: String? = null
) : Program {

    override fun execute() {
        create().execute()
    }

    private fun create(): Program {
        return when {
            pa.has_interactive_open_command_with_optional_options() -> {
                OpenPullRequestProgram(ui,
                        baseUrl,
                        repoUrl,
                        authToken,
                        PullRequestLastFinishedMarker())
            }
            else -> {
                OpenPullRequestProgram(ui,
                        baseUrl,
                        repoUrl,
                        authToken,
                        PullRequestLastNotFinishedMarker())
            }
        }
    }
}
