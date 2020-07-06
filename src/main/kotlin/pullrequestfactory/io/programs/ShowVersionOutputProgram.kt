package pullrequestfactory.io.programs

import pullrequestfactory.domain.Program
import pullrequestfactory.io.Properties

class ShowVersionOutputProgram : Program {

    override fun execute() {
        val projectVersion = Properties("app.properties").get_project_version()
        println("github-pr-factory version $projectVersion")
    }

}
