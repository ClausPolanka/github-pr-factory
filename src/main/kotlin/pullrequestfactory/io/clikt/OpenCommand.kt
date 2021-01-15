package pullrequestfactory.io.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.sources.PropertiesValueSource
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.impl.OpenPullRequestsProgram
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class OpenCommand(
        private val baseUrl: String,
        private val repoPath: String
) : CliktCommand(
        name = "open",
        help = """Opens pull requests of the candidate. If any option is not passed 
                 |then the app will prompt for it.""".trimMargin()) {
    init {
        context {
            valueSource = PropertiesValueSource.from("user.properties")
        }
    }

    private val cfn by candidateFirstNameOption()
    private val cln by candidateLastNameOption()
    private val githubToken by gitHubAuthorizationTokenOption()
    private val isLastFinished by isLastIterationFinishedFlag()
    private val pp1 by pairingPartner("1")
    private val pp2 by pairingPartner("2")
    private val pp3 by pairingPartner("3")
    private val pp4 by pairingPartner("4")
    private val pp5 by pairingPartner("5")
    private val pp6 by pairingPartner("6")
    private val pp7 by pairingPartner("7")

    override fun run() {
        val candidate = Candidate(cfn, cln)
        val httpClient = KhttpClientStats(KhttpClient(githubToken))
        val pps = listOf(pp1, pp2, pp3, pp4, pp5, pp6, pp7)
        OpenPullRequestsProgram(ConsoleUI(),
                baseUrl + repoPath,
                GithubAPIClient(httpClient, baseUrl),
                httpClient,
                isLastFinished,
                candidate,
                pps).execute()
    }
}
