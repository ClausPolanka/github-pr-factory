package pullrequestfactory.io

import pullrequestfactory.domain.Program

class Programs {

    fun create_program(args: Array<String>): Program {
        val programArgs = ProgramArgs(args, ConsoleUI())
        return if (programArgs.areValid()) {
            ValidProgram(programArgs)
        } else {
            InvalidProgram(programArgs)
        }
    }

}
