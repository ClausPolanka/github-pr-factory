package pullrequestfactory.io.programs

import pullrequestfactory.io.programs.impl.*

object Programs {

    fun create_program_for(args: Array<String>): Program {
        val pa = ProgramArgs(args)
        return when {
            pa.has_help_option() -> ShowHelpOutputProgram()
            pa.has_version_option() -> ShowVersionOutputProgram()
            pa.has_open_command_help_option() -> ShowOpenCommandHelpOutputProgram()
            pa.has_invalid_open_command() -> ShowInvalidOpenCommandOutputProgram()
            pa.has_open_command_with_optional_options() -> OpenPullRequestsProgramWithOptionalOptions(pa)
            pa.has_open_command() -> OpenPullRequestsProgram(pa)
            pa.has_close_command_help_option() -> ShowCloseCommandHelpOutputProgram()
            pa.has_invalid_close_command() -> ShowInvalidCloseCommandOutputProgram()
            pa.has_close_command_in_interactive_mode() -> ClosePullRequestsProgramInteractiveMode()
            pa.has_close_command() -> ClosePullRequestsProgram(pa)
            else -> ShowHelpOutputProgram()
        }
    }

}
