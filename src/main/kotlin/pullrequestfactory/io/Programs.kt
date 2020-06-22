package pullrequestfactory.io

import pullrequestfactory.domain.Program

class Programs {

    fun create_program(args: Array<String>): Program {
        val pa = ProgramArgsV2(args)
        if (pa.hasHelpOption()) {
            return ShowHelpOutputProgram()
        }
        if (pa.hasOpenCommandHelpOption()) {
            return ShowOpenHelpOutputProgram()
        }
        if (pa.hasOpenCommand()) {
            return CreatePullRequestsProgramV2(args)
        }
        if (pa.hasCloseCommandHelpOption()) {
            return ShowCloseHelpOutputProgram()
        }
        if (pa.hasCloseCommand()) {
            return ClosePullRequestsProgramV2(args)
        }
        val programArgs = ProgramArgs(args, ConsoleUI())
        return if (programArgs.areValid() && programArgs.isClosePullRequests()) {
            ClosePullRequestsProgram(programArgs)
        } else if (programArgs.areValid()) {
            CreatePullRequestsProgram(programArgs)
        } else {
            InvalidProgram(programArgs)
        }
    }

}
