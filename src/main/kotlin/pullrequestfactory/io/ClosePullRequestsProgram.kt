package pullrequestfactory.io

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.NoopCache
import pullrequestfactory.domain.Program

class ClosePullRequestsProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        val ui = ConsoleUI()
        val baseUrl = Properties("app.properties").getBaseUrl()
        val githubRepo = GithubHttpRepo(
                baseUrl,
                "wordcount",
                programArgs.basicAuthToken,
                NoopCache(),
                ui)
        val f = GithubPRFactory(githubRepo, githubRepo, ui)
        f.close_pull_requests_for(programArgs.candidate)
    }

}
