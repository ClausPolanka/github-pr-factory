package pullrequestfactory.io

import pullrequestfactory.domain.Program

class InvalidProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        programArgs.printErrorMessage()
    }

}
