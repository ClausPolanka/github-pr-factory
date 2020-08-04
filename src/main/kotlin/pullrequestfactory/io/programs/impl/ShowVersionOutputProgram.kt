package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowVersionOutputProgram : Program {

    private val properties = FileProperties("app.properties")

    override fun execute() {
        val projectVersion = properties.get_project_version()
        println("github-pr-factory version $projectVersion")
    }

}
