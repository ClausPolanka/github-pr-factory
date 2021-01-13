import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.sources.PropertiesValueSource
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val baseUrl = appProps.get_github_base_url()
    val repoPath = appProps.get_github_repository_path()
    OpenCommand(baseUrl, repoPath).main(arrayOf("-fn", "Claus", "-ln", "Polanka", "-l"))
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

    override fun run() {
        echo("Hello $cfn $cln $githubToken $isLastFinished")
        val candidate = Candidate(cfn, cln)
        val httpClient = KhttpClientStats(KhttpClient(githubToken))

//        OpenPullRequestsProgram(ConsoleUI(),
//                baseUrl + repoPath,
//                GithubAPIClient(httpClient, baseUrl),
//                httpClient,
//                isLastIterationFinished,
//                candidate,
//                pairingPartner).execute()
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
