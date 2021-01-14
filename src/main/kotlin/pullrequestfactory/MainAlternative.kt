import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.sources.PropertiesValueSource
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.programs.impl.OpenPullRequestsProgram
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val baseUrl = appProps.get_github_base_url()
    val repoPath = appProps.get_github_repository_path()
    OpenCommand(baseUrl, repoPath).main(args)
}

class OpenCommand(
        private val baseUrl: String,
        private val repoPath: String
) : CliktCommand(name = "open") {

    init {
        context {
            valueSource = PropertiesValueSource.from("user.properties")
        }
    }

    private val isLastFinished by isLastIterationFinishedFlag()
    private val cfn by candidateFirstNameOption()
    private val cln by candidateLastNameOption()
    private val githubToken by gitHubAuthorizationTokenOption()
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
        val pps = listOf(from(pp1), from(pp2), from(pp3), from(pp4), from(pp5), from(pp6), from(pp7))
        echo("Hello $cfn $cln $githubToken $isLastFinished $pps")
        OpenPullRequestsProgram(ConsoleUI(),
                baseUrl + repoPath,
                GithubAPIClient(httpClient, baseUrl),
                httpClient,
                isLastFinished,
                candidate,
                pps).execute()
    }
}

fun CliktCommand.isLastIterationFinishedFlag() =
        option("-l", "--last-finsished").flag()

fun CliktCommand.candidateFirstNameOption() =
        option("-fn", "--first-name").prompt("Candidate First Name")

fun CliktCommand.candidateLastNameOption() =
        option("-ln", "--last-name").prompt("Candidate Last Name")

fun CliktCommand.gitHubAuthorizationTokenOption() =
        option("-g", "--github-token").prompt("GitHub Authorization Token")

fun CliktCommand.pairingPartner(nr: String) =
        option("-pp$nr", "--pairing-partner-$nr", help = "Please chose from: ${PairingPartner.names()}")
                .prompt()

private fun from(pairingPartner: String): PairingPartner {
    val pp = PairingPartner.value_of(pairingPartner)
    return when (pp) {
        null -> throw ProgramArgs.WrongPairingPartnerArgumentSyntax("Unknown '$pairingPartner'")
        else -> pp
    }
}
