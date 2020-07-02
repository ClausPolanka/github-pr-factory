package pullrequestfactory.io

import pullrequestfactory.domain.Program

class ShowVersionOutputProgram : Program {

    override fun execute() {
        println("github-pr-factory version 1.0-SNAPSHOT")
    }

}
