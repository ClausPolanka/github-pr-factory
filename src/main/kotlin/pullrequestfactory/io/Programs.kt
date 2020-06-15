package pullrequestfactory.io

import pullrequestfactory.domain.Program

class Programs {

    fun create_program(args: Array<String>): Program {
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
