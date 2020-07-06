package pullrequestfactory.io

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.NoopCache
import pullrequestfactory.domain.Program

private const val repoPath = "/repos/ClausPolanka/wordcount"

class ClosePullRequestsProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val githubBasicAuthToken = programArgs.get_github_basic_auth_token()
        val ui = ConsoleUI()
        val baseUrl = Properties("app.properties").get_base_url()
        val githubRepo = GithubHttpRepo(
                baseUrl,
                repoPath,
                githubBasicAuthToken,
                NoopCache(),
                ui)
        val f = GithubPRFactory(githubRepo, githubRepo, ui)
        f.close_pull_requests_for(candidate)
    }

}
