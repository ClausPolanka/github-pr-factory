package pullrequestfactory.io.programs

import pullrequestfactory.domain.Program

class ShowInvalidCloseCommandOutputProgram : Program {

    override fun execute() {
        ShowCloseCommandHelpOutputProgram().execute()
    }

}
