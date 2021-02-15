package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.GithubAPIClient
import pullrequestfactory.io.repositories.KhttpClientStats

class ClosePullRequestsPrograms(
    private val ui: UI,
    private val githubAPIClient: GithubAPIClient,
    private val httpClientStats: KhttpClientStats,
    private val candidate: Candidate
) : Program {

    private val requiredNrOfRequestsForClosingPRs = 15

    override fun execute() {
        RateLimitCheckedPrograms(
            ui,
            githubAPIClient,
            httpClientStats,
            ClosePullRequestProgram(ui, githubAPIClient, candidate),
            requiredNrOfRequestsForClosingPRs
        ).instance(debug = true).execute()
    }

}
