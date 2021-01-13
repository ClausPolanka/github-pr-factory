import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.transformValues
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
    private val pairingPartner by pairingPartner()

    override fun run() {
        echo("Hello $cfn $cln $githubToken $isLastFinished $pairingPartner")
        val candidate = Candidate(cfn, cln)
        val httpClient = KhttpClientStats(KhttpClient(githubToken))
        OpenPullRequestsProgram(ConsoleUI(),
                baseUrl + repoPath,
                GithubAPIClient(httpClient, baseUrl),
                httpClient,
                isLastFinished,
                candidate,
                pairingPartner!!).execute()
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

fun CliktCommand.pairingPartner() = option("-p", "--pairing-partner", help = "Please chose 7 of: ${PairingPartner.names()}")
        .transformValues(7) {
            create_pairing_partner("${it[0]}-${it[1]}-${it[2]}-${it[3]}-${it[4]}-${it[5]}-${it[6]}")
        }

private val ERROR_MSG_PAIRING_PARTNER = "Either option -p or pairing partner are missing or pairing partner have wrong format or is unknown"

private fun create_pairing_partner(pairingPartner: String): List<PairingPartner> {
    return pairingPartner.split("-").map { br ->
        val pp = PairingPartner.value_of(br)
        when (pp) {
            null -> throw ProgramArgs.WrongPairingPartnerArgumentSyntax("$ERROR_MSG_PAIRING_PARTNER for given branch '$br'")
            else -> pp
        }
    }
}
