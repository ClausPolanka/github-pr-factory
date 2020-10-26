package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.KhttpClientStats

class ClosePullRequestsProgramInteractiveMode(
        private val ui: UI,
        private val repoUrl: String,
        private val basicAuthToken: String? = null) : Program {

    override fun execute() {
        show_welcome_message()
        val candidate = create_candidate_from_user_input()
        val token = create_basic_auth_token_from_user_input()
        close_pull_requests_for(candidate, token)
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

    private fun create_basic_auth_token_from_user_input(): String {
        if (basicAuthToken != null) {
            return basicAuthToken
        }
        val token = ui.get_user_input(msg = "Your Github.com basic authentication token: ")
        return token
    }

    private fun close_pull_requests_for(candidate: Candidate, githubBasicAuthToken: String) {
        val httpClient = KhttpClientStats()
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClient)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, githubBasicAuthToken, ui, httpClient)
        val f = GithubPRFactory(
                ui,
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(candidate)
        println()
        println(httpClient.stats())
    }

}
