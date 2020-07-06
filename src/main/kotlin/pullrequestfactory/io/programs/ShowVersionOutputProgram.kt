package pullrequestfactory.io.programs

import pullrequestfactory.domain.Program
import pullrequestfactory.io.FileProperties

class ShowVersionOutputProgram : Program {

    override fun execute() {
        val projectVersion = FileProperties("app.properties").get_project_version()
        println("github-pr-factory version $projectVersion")
    }

}
