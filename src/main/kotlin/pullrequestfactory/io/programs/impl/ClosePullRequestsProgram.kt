package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.uis.ConsoleUI

class ClosePullRequestsProgram(private val programArgs: ProgramArgs) : Program {

    private val properties = FileProperties("app.properties")

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val githubBasicAuthToken = programArgs.get_github_basic_auth_token()
        val baseUrl = properties.get_github_base_url()
        val repoPath = properties.get_github_repository_path()
        val ui = ConsoleUI()
        val githubBranchesRepo = GithubHttpBranchesRepos(baseUrl + repoPath, ui)
        val githubPullRequestsRepo = GithubHttpPullRequestsRepo(baseUrl + repoPath, githubBasicAuthToken, ui)
        val f = GithubPRFactory(githubBranchesRepo, githubPullRequestsRepo, BranchSyntaxValidator(ui))
        f.close_pull_requests_for(candidate)
    }

}
