package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo

class ClosePullRequestsProgramInteractiveMode(
        private val ui: UI,
        private val repoUrl: String) : Program {

    override fun execute() {
        ui.show("Welcome to interactive mode for closing pull requests")
        ui.show("Please provide data for the following questions")
        val candidateFirstName = ui.get_user_input(msg = "Candidate first name: ")
        val candidateLastName = ui.get_user_input(msg = "Candidate last name: ")
        val githubBasicAuthToken = ui.get_user_input(msg = "Your Github.com basic authorization token: ")
        val candidate = Candidate(candidateFirstName, candidateLastName)
        close_pull_requests_for(candidate, githubBasicAuthToken)
    }

    private fun close_pull_requests_for(candidate: Candidate, githubBasicAuthToken: String) {
        ui.show("Closing pull requests for: $candidate")
        val githubBranchesRepo = GithubHttpBranchesRepos(repoUrl, ui)
        val githubPullRequestsRepo = GithubHttpPullRequestsRepo(repoUrl, githubBasicAuthToken, ui)
        val f = GithubPRFactory(
                githubBranchesRepo,
                githubPullRequestsRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(candidate)
        ui.show("Successfully closed all pull requests for: $candidate")
        ui.show("Have a nice day. Bye bye.")
    }

}
