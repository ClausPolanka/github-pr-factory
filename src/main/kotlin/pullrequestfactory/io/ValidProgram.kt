package pullrequestfactory.io

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.NoopCache
import pullrequestfactory.domain.Program

class ValidProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        val baseUrl = this::class.java.classLoader.getResource("app.properties")?.readText()?.split("=")?.get(1)
        val githubRepo = GithubHttpRepo(
                baseUrl!!,
                "wordcount",
                programArgs.basicAuthToken,
                NoopCache(),
                ConsoleUI())
        val f = GithubPRFactory(githubRepo, githubRepo, ConsoleUI())
        f.create_pull_requests(programArgs.candidate, programArgs.pairingPartner)
    }

}
