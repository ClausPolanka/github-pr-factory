package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.*

class ClosePullRequestsProgramInteractiveMode(
        private val ui: UI,
        private val baseUrl: String,
        private val repoUrl: String,
        private val authToken: String? = null) : Program {

    private val requiredNrOfRequestsForClosingPRs = 15

    override fun execute() {
        show_welcome_message()
        val candidate = create_candidate_from_user_input()
        val token = create_auth_token_from_user_input()

        val httpClient = KhttpClient(token)
        val httpClientStats = KhttpClientStats(httpClient)
        RateLimitCheckedProgram(ui,
                GithubAPIClient(httpClient, baseUrl),
                httpClientStats,
                object : Program {
                    override fun execute() {
                        close_pull_requests_for(candidate, httpClientStats)
                    }
                },
                requiredNrOfRequestsForClosingPRs).execute()
    }

    private fun show_welcome_message() {
        ui.show("Welcome to interactive mode for closing pull requests")
        ui.show("Please provide data for the following questions")
    }

    private fun create_candidate_from_user_input(): Candidate {
        val firstName = ui.get_user_input(msg = "Candidate first name: ")
        val lastName = ui.get_user_input(msg = "Candidate last name: ")
        val candidate = Candidate(firstName, lastName)
        return candidate
    }

    private fun create_auth_token_from_user_input(): String {
        if (authToken != null) {
            return authToken
        }
        val token = ui.get_user_input(msg = "Your Github.com authentication token: ")
        return token
    }

    private fun close_pull_requests_for(candidate: Candidate, httpClient: HttpClient) {
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClient)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, ui, httpClient)
        val f = GithubPRFactory(
                ui,
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(candidate)
    }

}
