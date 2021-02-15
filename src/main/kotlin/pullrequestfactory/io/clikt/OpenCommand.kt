package pullrequestfactory.io.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.sources.PropertiesValueSource
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.programs.impl.OpenPullRequestsProgram
import pullrequestfactory.io.repositories.GithubAPIClient
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats

class OpenCommand(
    private val args: CommandArgs
) : CliktCommand(
    name = "open",
    help = """Opens pull requests. App prompts for options if not passed.""".trimMargin()
) {
    init {
        context {
            valueSource = PropertiesValueSource.from(args.userPropertiesFile)
        }
    }

    private val debug by option("-d", "--debug").flag(default = false)
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
        OpenPullRequestsProgram(
            args.ui,
            GithubAPIClient(httpClient, args.baseUrl, args.repoUrl, args.ui),
            httpClient,
            isLastFinished,
            candidate,
            pps,
            debug
        ).execute()
    }
}
