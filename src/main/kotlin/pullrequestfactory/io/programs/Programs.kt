package pullrequestfactory.io.programs

import pullrequestfactory.io.programs.impl.*

class Programs {

    fun create_program_for(args: Array<String>): Program {
        val pa = ProgramArgs(args)
        if (pa.has_help_option()) {
            return ShowHelpOutputProgram()
        }
        if (pa.has_version_option()) {
            return ShowVersionOutputProgram()
        }
        if (pa.has_open_command_help_option()) {
            return ShowOpenCommandHelpOutputProgram()
        }
        if (pa.has_invalid_open_command()) {
            return ShowInvalidOpenCommandOutputProgram()
        }
        if (pa.has_open_command_with_optional_options()) {
            return OpenPullRequestsProgramWithOptionalOptions(pa)
        }
        if (pa.has_open_command()) {
            return OpenPullRequestsProgram(pa)
        }
        if (pa.has_close_command_help_option()) {
            return ShowCloseCommandHelpOutputProgram()
        }
        if (pa.has_invalid_close_command()) {
            return ShowInvalidCloseCommandOutputProgram()
        }
        if (pa.has_close_command()) {
            return ClosePullRequestsProgram(pa)
        }
        return ShowHelpOutputProgram()
    }

}
