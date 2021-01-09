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

class `Github-Pr-Factory`: CliktCommand() {
    override fun run() = Unit
}

class Open: CliktCommand(help="Open pull requests for candidate") {
    val isLastIterationFinished by option(help = "Set if last iteration is finished", names = arrayOf("-l", "--last-finished")).flag()
    val candidate by option(help = "firstname-lastname", names = arrayOf("-c", "--candidate"))
    val pairingPartner by option(help = "pp1-pp2-pp3-pp4-pp5-pp6-pp7 | Please chose from: ${PairingPartner.names()}", names = arrayOf("-p", "--pairing-partner"))
    val githubToken by option(help = "Your personal GitHub authorization token", names = arrayOf("-g", "--github-token"))

    override fun run() {
        echo("Open Pull Requests for $candidate $isLastIterationFinished ")
    }
}

sealed class CloseMode(name: String): OptionGroup(name)

class CloseModeInteractive : CloseMode("interactive mode") {
    val candidateFirstName by option().prompt()
    val candidateLastName by option().prompt()
    val githubAuthorizationToken by option().prompt()
}

class CloseModeRegular: CloseMode("regular mode") {
    val candidate by option(names = arrayOf("-c", "--candidate")).required()
    val githubAuthorizationToken by option(names = arrayOf("-g", "--github-token")).required()
}

class Close : CliktCommand(help = "Close Pull Requests for Candidate") {
    val mode by option().groupChoice(
            "interactive" to CloseModeInteractive(),
            "regular" to CloseModeRegular()
    ).defaultByName("regular")

    override fun run() {
        when(val it = mode) {
            is CloseModeInteractive -> echo("Interactive Mode")
            is CloseModeRegular -> {
                echo("Closing Pull Requests for ${it.candidate}")
                echo("GitHub authorization token ${it.githubAuthorizationToken}")
            }
        }
    }
}

fun main(args: Array<String>) = `Github-Pr-Factory`()
        .subcommands(Open(), Close())
        .main(args)

