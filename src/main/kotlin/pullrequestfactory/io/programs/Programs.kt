package pullrequestfactory.io.programs

import pullrequestfactory.io.programs.impl.*
import pullrequestfactory.io.uis.ConsoleUI

object Programs {

    fun create_program_for(args: Array<String>): Program {
        val properties = FileProperties("app.properties")
        val ui = ConsoleUI()
        val pa = ProgramArgs(args)
        return when {
            pa.has_help_option() -> ShowHelpOutputProgram()
            pa.has_version_option() -> ShowVersionOutputProgram(properties)
            pa.has_open_command_help_option() -> ShowOpenCommandHelpOutputProgram()
            pa.has_invalid_open_command() -> ShowInvalidOpenCommandOutputProgram()
            pa.has_open_command_with_optional_options() -> OpenPullRequestsProgramWithOptionalOptions(pa, properties)
            pa.has_open_command() -> OpenPullRequestsProgram(pa, properties)
            pa.has_close_command_help_option() -> ShowCloseCommandHelpOutputProgram()
            pa.has_invalid_close_command() -> ShowInvalidCloseCommandOutputProgram()
            pa.has_close_command_in_interactive_mode() -> ClosePullRequestsProgramInteractiveMode(ui, properties)
            pa.has_close_command() -> ClosePullRequestsProgram(pa, properties)
            else -> ShowHelpOutputProgram()
        }
    }

}
