package pullrequestfactory.io

import pullrequestfactory.domain.Program

class Programs {

    fun create_program(args: Array<String>): Program {
        val pa = ProgramArgs(args)
        if (pa.has_help_option()) {
            return ShowHelpOutputProgram()
        }
        if (pa.has_open_command_help_option()) {
            return ShowOpenCommandHelpOutputProgram()
        }
        if (pa.has_invalid_open_command()) {
            return ShowInvalidOpenCommandOutputProgram()
        }
        if (pa.has_open_command()) {
            return CreatePullRequestsProgram(args)
        }
        if (pa.has_close_command_help_option()) {
            return ShowCloseCommandHelpOutputProgram()
        }
        if (pa.has_invalid_close_command()) {
            return ShowInvalidCloseCommandOutputProgram()
        }
        if (pa.has_close_command()) {
            return ClosePullRequestsProgram(args)
        }
        return ShowHelpOutputProgram()
    }

}
