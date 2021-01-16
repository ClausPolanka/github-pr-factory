package pullrequestfactory.io.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.context
import com.github.ajalt.clikt.sources.PropertiesValueSource
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.programs.impl.ClosePullRequestsPrograms
import pullrequestfactory.io.programs.impl.GithubAPIClient
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class CloseCommand(
        private val baseUrl: String,
        private val repoPath: String,
        private val userPropertiesFile: String
) : CliktCommand(
        name = "close",
        help = """Close pull requests of the candidate. If any option is not passed 
                 |then the app will prompt for it.""".trimMargin()) {
    init {
        context {
            valueSource = PropertiesValueSource.from(userPropertiesFile)
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
