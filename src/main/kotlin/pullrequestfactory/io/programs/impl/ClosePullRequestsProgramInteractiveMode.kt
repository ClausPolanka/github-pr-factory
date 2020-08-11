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
        val candidate = create_candidate()
        val token = ui.get_user_input(msg = "Your Github.com basic authorization token: ")
        close_pull_requests_for(candidate, token)
    }

    private fun create_candidate(): Candidate {
        val firstName = ui.get_user_input(msg = "Candidate first name: ")
        val lastName = ui.get_user_input(msg = "Candidate last name: ")
        val candidate = Candidate(firstName, lastName)
        return candidate
    }

    private fun close_pull_requests_for(candidate: Candidate, githubBasicAuthToken: String) {
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, githubBasicAuthToken, ui)
        val f = GithubPRFactory(
                ui,
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(candidate)
    }

}
