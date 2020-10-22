package pullrequestfactory.io.programs

import pullrequestfactory.io.programs.impl.*
import pullrequestfactory.io.uis.ConsoleUI

object Programs {

    fun create_program_for(args: Array<String>): Program {
        val ui = ConsoleUI()
        val appProps = FileAppProperties("app.properties")
        val userProps = FileUserProperties("userr.properties")
        val baseUrl = appProps.get_github_base_url()
        val repoPath = appProps.get_github_repository_path()
        val basicAuthToken = userProps.get_github_basic_auth_token()
        val pa = ProgramArgs(args, basicAuthToken)
        val repoUrl = baseUrl + repoPath
        return when {
            pa.has_help_option() -> ShowHelpOutputProgram()
            pa.has_version_option() -> ShowVersionOutputProgram(appProps)
            pa.has_open_command_help_option() -> ShowOpenCommandHelpOutputProgram()
            pa.has_invalid_open_command() -> ShowInvalidOpenCommandOutputProgram()
            pa.has_open_command_in_interactive_mode() -> OpenPullRequestsProgramInteractiveMode(ui, repoUrl, basicAuthToken)
            pa.has_open_command_with_optional_options() -> OpenPullRequestsProgramWithOptionalOptions(ui, pa, repoUrl)
            pa.has_open_command() -> OpenPullRequestsProgram(ui, pa, repoUrl)
            pa.has_close_command_help_option() -> ShowCloseCommandHelpOutputProgram()
            pa.has_invalid_close_command() -> ShowInvalidCloseCommandOutputProgram()
            pa.has_close_command_in_interactive_mode() -> ClosePullRequestsProgramInteractiveMode(ui, repoUrl, basicAuthToken)
            pa.has_close_command() -> ClosePullRequestsProgram(ui, pa, repoUrl)
            else -> ShowHelpOutputProgram()
        }
    }

}
