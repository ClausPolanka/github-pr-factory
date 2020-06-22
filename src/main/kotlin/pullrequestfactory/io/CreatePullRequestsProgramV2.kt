package pullrequestfactory.io

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.NoopCache
import pullrequestfactory.domain.Program

class CreatePullRequestsProgramV2(private val args: Array<String>) : Program {

    override fun execute() {
        val candidateFirstName = args[args.indexOf("-c") + 1].split("-")[0]
        val candidateLastName = args[args.indexOf("-c") + 1].split("-")[1]
        val githubBasicAuthToken = args[args.indexOf("-g") + 1]
        val pairingPartner = args[args.indexOf("-p") + 1].split("-")
        val ui = ConsoleUI()
        val baseUrl = Properties("app.properties").getBaseUrl()
        val githubRepo = GithubHttpRepo(
                baseUrl,
                "wordcount",
                githubBasicAuthToken,
                NoopCache(),
                ui)
        val f = GithubPRFactory(githubRepo, githubRepo, ui)
        f.create_pull_requests(Candidate(candidateFirstName, candidateLastName), pairingPartner)
    }

}
