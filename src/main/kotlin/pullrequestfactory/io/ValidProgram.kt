package pullrequestfactory.io

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.NoopCache
import pullrequestfactory.domain.Program

class ValidProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        val ui = ConsoleUI()
        val baseUrl = Properties("app.properties", ui).getBaseUrl()
        val githubRepo = GithubHttpRepo(
                baseUrl,
                "wordcount",
                programArgs.basicAuthToken,
                NoopCache(),
                ui)
        val f = GithubPRFactory(githubRepo, githubRepo, ui)
        f.create_pull_requests(programArgs.candidate, programArgs.pairingPartner)
    }

}
