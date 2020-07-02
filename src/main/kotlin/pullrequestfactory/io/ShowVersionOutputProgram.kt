package pullrequestfactory.io

import pullrequestfactory.domain.Program

class ShowVersionOutputProgram : Program {

    override fun execute() {
        val projectVersion = Properties("app.properties").get_project_version()
        println("github-pr-factory version $projectVersion")
    }

}
