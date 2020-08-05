package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowCloseCommandHelpOutputProgram : Program {

    override fun execute() {
        val helpOutput = """
        Usage: github-pr-factory close [OPTION]
        
        Close Github pull requests for a 2nd round hiring candidate
        
        Options:
          -c${"\t\t"}The candidate's first name and last-name separated by hyphen
          -g${"\t\t"}Your Github basic authorization token
        
        Example:
          github-pr-factory close -c firstname-lastname -g 10238sadf08klasjdf098
        """
        println(helpOutput.trimIndent())
    }

}
