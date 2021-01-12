package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.pullrequests.PullRequestLastFinishedMarker
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program

class OpenPullRequestsProgramsInteractiveMode(
        private val ui: UI,
        private val baseUrl: String,
        private val repoUrl: String,
        private val authToken: String? = null,
        private val isLastIterationFinished: Boolean
) : Program {

    override fun execute() {
        create().execute()
    }

    private fun create(): Program {
        return when (isLastIterationFinished) {
            true -> {
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
