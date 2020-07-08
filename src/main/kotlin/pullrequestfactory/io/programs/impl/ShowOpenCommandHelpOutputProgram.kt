package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowOpenCommandHelpOutputProgram : Program {

    override fun execute() {
        println("Usage: github-pr-factory open [OPTION]")
        println("")
        println("Open new Github pull requests for a 2nd round hiring candidate")
        println("")
        println("Options:")
        println("  -c\t\tThe candidate's first name and last-name separated by hyphen")
        println("  -g\t\tYour Github basic authorization token")
        println("  -p\t\tSeven pairing-partner names separated by hyphen")
        println("    \t\t  Currently supported names:")
        println("    \t\t  claus, berni, bernhard, nandor, dominik, mihai, lampe, shubi")
        println("    \t\t  markus, tibor, christian, michal, tomas, peter, martin, john, andrej")
        println("")
        println("  -l, --last-finished\t\tMarks last iteration pull request with '[PR]' as finished")
        println("")
        println("Example:")
        println("  github-pr-factory open -c firstname-lastname \\")
        println("    -g 10238sadf08klasjdf098 \\")
        println("    -p claus-berni-nandor-dominik-mihai-lampe-shubi")
    }

}
