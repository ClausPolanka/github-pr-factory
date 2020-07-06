package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowInvalidOpenCommandOutputProgram : Program {

    override fun execute() {
        ShowOpenCommandHelpOutputProgram().execute()
    }

}
