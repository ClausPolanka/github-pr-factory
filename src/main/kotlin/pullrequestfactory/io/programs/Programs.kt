package pullrequestfactory.io.programs

import pullrequestfactory.io.programs.impl.*
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
        return when {
            pa.has_help_option() -> ShowHelpOutputProgram()
            pa.has_version_option() -> ShowVersionOutputProgram(appProps)
            pa.has_open_command_help_option() -> ShowOpenCommandHelpOutputProgram()
            pa.has_invalid_open_command() -> ShowInvalidOpenCommandOutputProgram()
            pa.has_open_command_in_interactive_mode() -> OpenPullRequestsProgramInteractiveMode(ui, baseUrl, repoUrl, authTokenFromProps)
            pa.has_open_command() -> OpenPullRequestsProgram(ui, pa, baseUrl, repoUrl, pa.get_github_auth_token())
            pa.has_close_command_help_option() -> ShowCloseCommandHelpOutputProgram()
            pa.has_invalid_close_command() -> ShowInvalidCloseCommandOutputProgram()
            pa.has_close_command_in_interactive_mode() -> ClosePullRequestsProgramInteractiveMode(ui, repoUrl, authTokenFromProps)
            pa.has_close_command() -> ClosePullRequestsPrograms(ui, pa, baseUrl, repoUrl, pa.get_github_auth_token())
            else -> ShowHelpOutputProgram()
        }
    }

}
