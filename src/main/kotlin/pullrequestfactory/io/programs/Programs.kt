package pullrequestfactory.io.programs

import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.impl.*
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

object Programs {

    fun create_program_for(args: Array<String>): Program {
        val ui = ConsoleUI()
        val appProps = FileAppProperties("app.properties")
        val baseUrl = appProps.get_github_base_url()
        val repoPath = appProps.get_github_repository_path()
        val userProps = FileUserProperties("user.properties")
        val authTokenFromProps = userProps.get_github_auth_token()
        val pa = ProgramArgs(args, authTokenFromProps)
        val repoUrl = baseUrl + repoPath
        val g by lazy { GitHubHttp(pa, baseUrl) }
        return when {
            pa.has_help_option() -> ShowHelpOutputProgram()
            pa.has_version_option() -> ShowVersionOutputProgram(appProps)
            pa.has_open_command_help_option() -> ShowOpenCommandHelpOutputProgram()
            pa.has_invalid_open_command() -> ShowInvalidOpenCommandOutputProgram()
            pa.has_open_command_in_interactive_mode() -> OpenPullRequestsProgramsInteractiveMode(ui, baseUrl, repoUrl, authTokenFromProps, pa.has_interactive_open_command_with_optional_options())
            pa.has_open_command() -> OpenPullRequestsProgram(ui, repoUrl, g.github_api_client(), g.http_client_stats(), pa.has_open_command_with_optional_options(), pa.get_candidate(), pa.get_pairing_partner())
            pa.has_close_command_help_option() -> ShowCloseCommandHelpOutputProgram()
            pa.has_invalid_close_command() -> ShowInvalidCloseCommandOutputProgram()
            pa.has_close_command_in_interactive_mode() -> ClosePullRequestsProgramInteractiveMode(ui, baseUrl, repoUrl, authTokenFromProps)
            pa.has_close_command() -> ClosePullRequestsPrograms(ui, repoUrl, g.github_api_client(), g.http_client_stats(), pa.get_candidate())
            else -> ShowHelpOutputProgram()
        }
    }

}

class GitHubHttp(
        programArgs: ProgramArgs,
        private val baseUrl: String
) {

    val httpClient = KhttpClient(programArgs.get_github_auth_token())

    fun github_api_client(): GithubAPIClient {
        return GithubAPIClient(httpClient, baseUrl)
    }

    fun http_client_stats(): KhttpClientStats {
        return KhttpClientStats(httpClient)
    }

}
