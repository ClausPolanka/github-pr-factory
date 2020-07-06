package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowInvalidCloseCommandOutputProgram : Program {

    override fun execute() {
        ShowCloseCommandHelpOutputProgram().execute()
    }

}
