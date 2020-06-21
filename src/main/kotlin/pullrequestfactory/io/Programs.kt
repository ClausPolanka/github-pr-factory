package pullrequestfactory.io

import pullrequestfactory.domain.Program

class Programs {

    fun create_program(args: Array<String>): Program {
        if (args.isEmpty() || args[0] == "-?" || args[0] == "--help") {
            return ShowHelpOutputProgram()
        }
        if (args[0] == "open" && args[1] == "--help") {
            return ShowOpenHelpOutputProgram()
        }
        if (args[0] == "close" && args[1] == "--help") {
            return ShowCloseHelpOutputProgram()
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
