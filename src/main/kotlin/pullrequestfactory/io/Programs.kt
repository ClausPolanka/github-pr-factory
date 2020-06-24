package pullrequestfactory.io

import pullrequestfactory.domain.Program

class Programs {

    fun create_program(args: Array<String>): Program {
        val pa = ProgramArgsV2(args)
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
            return CreatePullRequestsProgramV2(args)
        }
        if (pa.has_close_command_help_option()) {
            return ShowCloseCommandHelpOutputProgram()
        }
        if (pa.has_invalid_close_command()) {
            return ShowInvalidCloseCommandOutputProgram()
        }
        if (pa.has_close_command()) {
            return ClosePullRequestsProgramV2(args)
        }
        return ShowHelpOutputProgram()
    }

}
