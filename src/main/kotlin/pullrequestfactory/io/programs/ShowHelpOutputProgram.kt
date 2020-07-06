package pullrequestfactory.io.programs

import pullrequestfactory.domain.Program

class ShowHelpOutputProgram : Program {

    override fun execute() {
        println("Usage: github-pr-factory [OPTION] COMMAND")
        println("")
        println("A tool which helps opening and closing Github pull requests")
        println("for the George backend chapter 2nd round hirings.")
        println("")
        println("Options:")
        println("  -?, --help\t\t\tPrint this help statement")
        println("  -v, --version\t\t\tPrint version information and quit")
        println("")
        println("Commands:")
        println("  open\t\tOpen Github pull requests")
        println("  close\t\tOpen Github pull requests")
        println("")
        println("Run 'github-pr-factory COMMAND --help' for more information on a command.")
    }

}
