package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowOpenCommandHelpOutputProgram : Program {

    override fun execute() {
        val helpOutput = """
        Usage: github-pr-factory open [OPTION]

        Open new Github pull requests for a 2nd round hiring candidate

        Options:
          -i, --interactive${"\t\t"}Interactive flow of opening new pull requests

          -c${"\t\t"}The candidate's first name and last-name separated by hyphen
          -g${"\t\t"}Your Github basic authorization token
          -p${"\t\t"}Seven pairing-partner names separated by hyphen
            ${"\t\t"}  Currently supported names:
            ${"\t\t"}  claus, berni, bernhard, nandor, dominik, mihai, lampe, shubi
            ${"\t\t"}  markus, tibor, christian, michal, tomas, peter, martin, john, andrej

          -l, --last-finished${"\t\t"}Marks last iteration pull request with '[PR]' as finished

        Example:
          github-pr-factory open -c firstname-lastname ${"\\"}
            -g 10238sadf08klasjdf098 ${"\\"}
            -p claus-berni-nandor-dominik-mihai-lampe-shubi
        """
        println(helpOutput.trimIndent())
    }

}
