package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowCloseCommandHelpOutputProgram : Program {

    override fun execute() {
        println("Usage: github-pr-factory close [OPTION]")
        println("")
        println("Close Github pull requests for a 2nd round hiring candidate")
        println("")
        println("Options:")
        println("  -c\t\tThe candidate's first name and last-name separated by hyphen")
        println("  -g\t\tYour Github basic authorization token")
        println("")
        println("Example:")
        println("  github-pr-factory close -c firstname-lastname -g 10238sadf08klasjdf098")
    }

}
