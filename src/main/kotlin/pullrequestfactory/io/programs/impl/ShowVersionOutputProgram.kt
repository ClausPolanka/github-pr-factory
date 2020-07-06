package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowVersionOutputProgram : Program {

    override fun execute() {
        val projectVersion = FileProperties("app.properties").get_project_version()
        println("github-pr-factory version $projectVersion")
    }

}
