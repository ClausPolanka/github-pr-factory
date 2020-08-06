package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.Properties

class ShowVersionOutputProgram(private val properties: Properties) : Program {

    override fun execute() {
        val projectVersion = properties.get_project_version()
        println("github-pr-factory version $projectVersion")
    }

}
