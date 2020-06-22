package pullrequestfactory.io

import pullrequestfactory.domain.Program

class ShowInvalidCloseCommandOutputProgram : Program {

    override fun execute() {
        ShowCloseCommandHelpOutputProgram().execute()
    }

}
