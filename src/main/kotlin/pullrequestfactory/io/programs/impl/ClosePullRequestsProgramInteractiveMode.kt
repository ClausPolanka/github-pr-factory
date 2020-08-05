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
        var candidateFirstName = readLine()
        while (candidateFirstName.isNullOrEmpty()) {
            print("Candidate first name: ")
            candidateFirstName = readLine()
        }

        print("Candidate last name: ")
        var candidateLastName = readLine()
        while (candidateLastName.isNullOrEmpty()) {
            print("Candidate last name: ")
            candidateLastName = readLine()
        }

        print("Your Github.com basic authorization token: ")
        var githubBasicAuthToken = readLine()
        while (githubBasicAuthToken.isNullOrEmpty()) {
            print("Your Github.com basic authorization token: ")
            githubBasicAuthToken = readLine()
        }

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
