import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.versionOption
import com.github.ajalt.clikt.parameters.types.enum
import com.github.ajalt.clikt.sources.PropertiesValueSource
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.impl.ClosePullRequestsPrograms
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.programs.impl.OpenPullRequestsProgram
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val baseUrl = appProps.get_github_base_url()
    val repoPath = appProps.get_github_repository_path()
    GitHubPrFactory(appProps.get_project_version())
            .subcommands(OpenCommand(baseUrl, repoPath), CloseCommand(baseUrl, repoPath))
            .main(args)
}

class GitHubPrFactory(appVersion: String) : CliktCommand(name = "github-pr-factory") {
    init {
        versionOption(appVersion)
    }
    override fun run() = Unit
}

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

class CloseCommand(
        private val baseUrl: String,
        private val repoPath: String
) : CliktCommand(
        name = "close",
        help = """Close pull requests of the candidate. If any option is not passed 
                 |then the app will prompt for it.""".trimMargin()) {
    init {
        context {
            valueSource = PropertiesValueSource.from("user.properties")
        }
    }

    private val githubToken by gitHubAuthorizationTokenOption()
    private val cfn by candidateFirstNameOption()
    private val cln by candidateLastNameOption()

    override fun run() {
        val candidate = Candidate(cfn, cln)
        val httpClient = KhttpClientStats(KhttpClient(githubToken))
        ClosePullRequestsPrograms(ConsoleUI(),
                baseUrl + repoPath,
                GithubAPIClient(httpClient, baseUrl),
                httpClient,
                candidate).execute()
    }
}

fun CliktCommand.isLastIterationFinishedFlag() =
        option("-l", "--last-finsished", help = "Was the last iteration finished by the candidate?")
                .flag()

fun CliktCommand.candidateFirstNameOption() =
        option("-fn", "--first-name", help = "Candidate's first name")
                .prompt("Candidate First Name")

fun CliktCommand.candidateLastNameOption() =
        option("-ln", "--last-name", help = "Candidate's last name")
                .prompt("Candidate Last Name")

fun CliktCommand.gitHubAuthorizationTokenOption() =
        option("-g", "--github-token", help = """Your personal GitHub authorization token. 
            |Can be set in a file user.properties in the root directory. The file's format: 
            |"github-token=<your-token>."""".trimMargin(), valueSourceKey = "github-token")
                .prompt("GitHub Authorization Token")

fun CliktCommand.pairingPartner(nr: String) =
        option("-pp$nr", "--pairing-partner-$nr")
                .enum<PairingPartner>()
                .prompt()
