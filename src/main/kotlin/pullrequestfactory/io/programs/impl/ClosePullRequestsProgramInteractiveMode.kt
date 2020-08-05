package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.uis.ConsoleUI

class ClosePullRequestsProgramInteractiveMode : Program {

    private val properties = FileProperties("app.properties")

    override fun execute() {
        println("Welcome to interactive mode for closing pull requests")
        println("Please provide data for the following questions")

        print("Candidate first name: ")
        val candidateFirstName = readLine() ?: "invalid first name"

        print("Candidate last name: ")
        val candidateLastName = readLine() ?: "invalid last name"

        print("Your Github.com basic authorization token: ")
        val githubBasicAuthToken = readLine()
                ?: "invalid Github basic auth token"

        val candidate = Candidate(candidateFirstName, candidateLastName)

        println("Closing pull requests for: $candidate")
        val baseUrl = properties.get_github_base_url()
        val repoPath = properties.get_github_repository_path()
        val ui = ConsoleUI()
        val githubBranchesRepo = GithubHttpBranchesRepos(baseUrl + repoPath, ui)
        val githubPullRequestsRepo = GithubHttpPullRequestsRepo(baseUrl + repoPath, githubBasicAuthToken, ui)
        val f = GithubPRFactory(
                githubBranchesRepo,
                githubPullRequestsRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(candidate)
        println("Successfully closed all pull requests for: $candidate")
        println("Have a nice day. Bye bye.")
    }

}
