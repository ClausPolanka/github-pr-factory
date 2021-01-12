import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.programs.impl.FileUserProperties
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val baseUrl = appProps.get_github_base_url()
    val repoPath = appProps.get_github_repository_path()

    when (val authToken = get_token_from_user_properties()) {
        null -> OpenCommand(baseUrl, repoPath).main(args)
        else -> OpenCommandWhenTokenExists(baseUrl, repoPath, authToken).main(args)
    }
}

class OpenCommand(
        private val baseUrl: String,
        private val repoPath: String
) : CliktCommand(name = "open") {

    private val isLastFinished by isLastIterationFinishedFlag()
    private val cfn by candidateFirstNameOption()
    private val cln by candidateLastNameOption()
    private val token by gitHubAuthorizationTokenOption()

    override fun run() {
        echo("Hello $cfn $cln $token $isLastFinished")
        val candidate = Candidate(cfn, cln)
        val httpClient = KhttpClientStats(KhttpClient(token))

//        OpenPullRequestsProgram(ConsoleUI(),
//                baseUrl + repoPath,
//                GithubAPIClient(httpClient, baseUrl),
//                httpClient,
//                isLastIterationFinished,
//                candidate,
//                pairingPartner).execute()
    }
}

class OpenCommandWhenTokenExists(
        private val baseUrl: String,
        private val repoPath: String,
        private val token: String
) : CliktCommand(name = "open") {

    private val isLastFinished by isLastIterationFinishedFlag()
    private val cfn by candidateFirstNameOption()
    private val cln by candidateLastNameOption()

    override fun run() {
        echo("Hello $cfn $cln $token $isLastFinished")
        val candidate = Candidate(cfn, cln)
        val httpClient = KhttpClientStats(KhttpClient(token))

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
        option(names = arrayOf("-l", "--last-finsished")).flag()

fun CliktCommand.candidateFirstNameOption() =
        option(names = arrayOf("-fn", "--first-name")).prompt("Candidate First Name")

fun CliktCommand.candidateLastNameOption() =
        option(names = arrayOf("-ln", "--last-name")).prompt("Candidate Last Name")

fun CliktCommand.gitHubAuthorizationTokenOption() =
        option(names = arrayOf("-g", "--github-token"))
                .prompt("GitHub Authorization Token", default = get_token_from_user_properties())

private fun get_token_from_user_properties(): String? {
    val userProps = FileUserProperties("user.properties")
    val authTokenFromProps = userProps.get_github_auth_token()
    return authTokenFromProps
}
