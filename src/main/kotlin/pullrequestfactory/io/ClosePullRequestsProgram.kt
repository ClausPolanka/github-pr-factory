package pullrequestfactory.io

import pullrequestfactory.domain.*

class ClosePullRequestsProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val githubBasicAuthToken = programArgs.get_github_basic_auth_token()
        val baseUrl = Properties("app.properties").get_base_url()
        val repoPath = Properties("app.properties").get_github_repository_path()
        val githubRepo = GithubHttpRepo(
                baseUrl,
                repoPath,
                githubBasicAuthToken,
                NoopCache(),
                QuietUI())
        val f = GithubPRFactory(githubRepo, githubRepo, BranchSyntaxValidator(ConsoleUI()))
        f.close_pull_requests_for(candidate)
    }

}
