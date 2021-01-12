package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.KhttpClientStats

class OpenPullRequestsProgram(
        private val ui: UI,
        private val repoUrl: String,
        private val githubAPIClient: GithubAPIClient,
        private val httpClientStats: KhttpClientStats,
        private val isLastIterationFinished: Boolean,
        private val candidate: Candidate,
        private val pairingPartner: List<PairingPartner>
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
        return when (isLastIterationFinished) {
            true -> {
                OpenPRProgramLastSessionFinished(ui,
                        repoUrl,
                        httpClientStats,
                        candidate,
                        pairingPartner
                )
            }
            else -> OpenPRsProgramLastSessionNotFinished(ui,
                    repoUrl,
                    httpClientStats,
                    candidate,
                    pairingPartner
            )
        }
    }

}

