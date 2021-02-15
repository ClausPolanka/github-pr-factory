package pullrequestfactory.io.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.sources.PropertiesValueSource
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.programs.impl.ClosePullRequestsPrograms
import pullrequestfactory.io.repositories.GithubAPIClient
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats

class CloseCommand(
    private val args: CommandArgs
) : CliktCommand(
    name = "close",
    help = """Closes pull requests. App prompts for options if not passed.""".trimMargin()
) {
    init {
        context {
            valueSource = PropertiesValueSource.from(args.userPropertiesFile)
        }
    }

    private val debug by debugOption()
    private val githubToken by gitHubAuthorizationTokenOption()
    private val cfn by candidateFirstNameOption()
    private val cln by candidateLastNameOption()

    override fun run() {
        val candidate = Candidate(cfn, cln)
        val httpClient = KhttpClientStats(KhttpClient(githubToken))
        ClosePullRequestsPrograms(
            args.ui,
            GithubAPIClient(httpClient, args.baseUrl, args.repoUrl, args.ui),
            httpClient,
            candidate,
            debug
        ).execute()
    }
}
