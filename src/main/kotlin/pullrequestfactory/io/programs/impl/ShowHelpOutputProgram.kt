package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.Program

class ShowHelpOutputProgram : Program {

    override fun execute() {
        val helpOutput = """
        Usage: github-pr-factory [OPTION] COMMAND
        
        A tool which helps opening and closing Github pull requests
        for the George backend chapter 2nd round hirings.
        
        Options:
          -?, --help${"\t\t\t"}Print this help statement
          -v, --version${"\t\t\t"}Print version information and quit
        
        Commands:
          open${"\t\t"}Open Github pull requests
          close${"\t\t"}Close Github pull requests
        
        Run 'github-pr-factory COMMAND --help' for more information on a command.
        """
        println(helpOutput.trimIndent())
    }

}
