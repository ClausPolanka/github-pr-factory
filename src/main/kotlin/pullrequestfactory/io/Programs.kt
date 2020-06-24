package pullrequestfactory.io

import pullrequestfactory.domain.Program

class Programs {

    fun create_program(args: Array<String>): Program {
        val pa = ProgramArgsV2(args)
        if (pa.hasHelpOption()) {
            return ShowHelpOutputProgram()
        }
        if (pa.hasOpenCommandHelpOption()) {
            return ShowOpenCommandHelpOutputProgram()
        }
        if (pa.hasInvalidOpenCommand()) {
            return ShowInvalidOpenCommandOutputProgram()
        }
        if (pa.hasOpenCommand()) {
            return CreatePullRequestsProgramV2(args)
        }
        if (pa.hasCloseCommandHelpOption()) {
            return ShowCloseCommandHelpOutputProgram()
        }
        if (pa.hasInvalidCloseCommand()) {
            return ShowInvalidCloseCommandOutputProgram()
        }
        if (pa.hasCloseCommand()) {
            return ClosePullRequestsProgramV2(args)
        }
        return ShowHelpOutputProgram()
    }

}
