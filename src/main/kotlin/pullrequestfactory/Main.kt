package pullrequestfactory

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.groups.OptionGroup
import com.github.ajalt.clikt.parameters.groups.defaultByName
import com.github.ajalt.clikt.parameters.groups.groupChoice
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.options.required
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.io.clikt.CliktClosePRsFactory
import pullrequestfactory.io.clikt.CliktOpenPRsFactory
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.uis.ConsoleUI

class `Github-Pr-Factory` : CliktCommand() {
    override fun run() = Unit
}

sealed class OpenMode(name: String) : OptionGroup(name) {
    val isLastIterationFinished by option(help = "Set if last iteration is finished", names = arrayOf("-l", "--last-finished")).flag()
}

class OpenModeInteractive : OpenMode("interactive mode") {
    val candidateFirstName by option().prompt()
    val candidateLastName by option().prompt()
    val githubAuthorizationToken by option().prompt()
    val pairingPartner1 by option().prompt()
    val pairingPartner2 by option().prompt()
    val pairingPartner3 by option().prompt()
    val pairingPartner4 by option().prompt()
    val pairingPartner5 by option().prompt()
    val pairingPartner6 by option().prompt()
    val pairingPartner7 by option().prompt()
}

class OpenModeRegular : OpenMode("regular mode") {
//    val isLastIterationFinished by option(help = "Set if last iteration is finished", names = arrayOf("-l", "--last-finished")).flag()
    val candidate by option(help = "firstname-lastname", names = arrayOf("-c", "--candidate"))
    val pairingPartner by option(help = "pp1-pp2-pp3-pp4-pp5-pp6-pp7 | Please chose from: ${PairingPartner.names()}", names = arrayOf("-p", "--pairing-partner"))
    val githubToken by option(help = "Your personal GitHub authorization token", names = arrayOf("-g", "--github-token"))
}

class Open : CliktCommand(help = "Open Pull Requests for Candidate") {
    private val mode by option(names = arrayOf("-m", "--mode")).groupChoice(
            "interactive" to OpenModeInteractive(),
            "regular" to OpenModeRegular()
    ).defaultByName("regular")

    override fun run() {
        val appProps = FileAppProperties("app.properties")
        val baseUrl = appProps.get_github_base_url()
        val repoPath = appProps.get_github_repository_path()
        val program = CliktOpenPRsFactory(ConsoleUI(), baseUrl, repoPath).create_for(mode)
        program.execute()
    }

}

sealed class CloseMode(name: String) : OptionGroup(name)

class CloseModeInteractive : CloseMode("interactive mode") {
    val candidateFirstName by option().prompt()
    val candidateLastName by option().prompt()
    val githubAuthorizationToken by option().prompt()
}

class CloseModeRegular : CloseMode("regular mode") {
    val candidate by option(names = arrayOf("-c", "--candidate")).required()
    val githubAuthorizationToken by option(names = arrayOf("-g", "--github-token")).required()
}

class Close : CliktCommand(help = "Close Pull Requests for Candidate") {
    private val mode by option(names = arrayOf("-m", "--mode")).groupChoice(
            "interactive" to CloseModeInteractive(),
            "regular" to CloseModeRegular()
    ).defaultByName("regular")

    override fun run() {
        val appProps = FileAppProperties("app.properties")
        val baseUrl = appProps.get_github_base_url()
        val repoPath = appProps.get_github_repository_path()
        val program = CliktClosePRsFactory(ConsoleUI(), baseUrl + repoPath).create_for(mode)
        program.execute()
    }

}

fun main(args: Array<String>) = `Github-Pr-Factory`()
        .subcommands(Open(), Close())
        .main(args)

