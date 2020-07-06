package pullrequestfactory.io.programs

import pullrequestfactory.domain.Program

class ShowInvalidOpenCommandOutputProgram : Program {

    override fun execute() {
        ShowOpenCommandHelpOutputProgram().execute()
    }

}
