package pullrequestfactory.io

import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.Program

class ValidProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        val githubRepo = GithubHttpRepo("wordcount", programArgs.basicAuthToken)
        val f = GithubPRFactory(githubRepo, githubRepo)
        f.create_pull_requests(programArgs.candidate, programArgs.pairingPartner)
    }

}
