package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.AppProperties
import pullrequestfactory.io.programs.Program

class ShowVersionOutputProgram(private val appProperties: AppProperties) : Program {

    override fun execute() {
        val projectVersion = appProperties.get_project_version()
        println("github-pr-factory version $projectVersion")
    }

}
