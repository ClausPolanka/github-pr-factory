package pullrequestfactory.io.programs

import pullrequestfactory.domain.BranchSyntaxValidator
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.Program
import pullrequestfactory.domain.QuietUI
import pullrequestfactory.io.ConsoleUI
import pullrequestfactory.io.FileProperties
import pullrequestfactory.io.ProgramArgs
import pullrequestfactory.io.factories.GithubRepos
import pullrequestfactory.io.repositories.GithubHttpWriteRepo

class ClosePullRequestsProgram(private val programArgs: ProgramArgs) : Program {

    private val properties = FileProperties("app.properties")

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val githubBasicAuthToken = programArgs.get_github_basic_auth_token()
        val baseUrl = properties.get_github_base_url()
        val repoPath = properties.get_github_repository_path()
        val githubWriteRepo = GithubHttpWriteRepo(baseUrl + repoPath, githubBasicAuthToken, QuietUI())
        val githubReadRepo = GithubRepos(baseUrl + repoPath, QuietUI())
        val f = GithubPRFactory(githubReadRepo, githubWriteRepo, BranchSyntaxValidator(ConsoleUI()))
        f.close_pull_requests_for(candidate)
    }

}
