package pullrequestfactory.io.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.prompt
import com.github.ajalt.clikt.parameters.types.enum
import pullrequestfactory.domain.PairingPartner

fun CliktCommand.isLastIterationFinishedFlag() =
        option("-l", "--last-finished", help = "Was the last iteration finished by the candidate?")
                .flag()

fun CliktCommand.candidateFirstNameOption() =
        option("-fn", "--first-name", help = "Candidate's first name")
                .prompt("Candidate First Name")

fun CliktCommand.candidateLastNameOption() =
        option("-ln", "--last-name", help = "Candidate's last name")
                .prompt("Candidate Last Name")

fun CliktCommand.gitHubAuthorizationTokenOption() =
        option("-g", "--github-token", help = """Your personal GitHub authorization token. 
            |Can be set in a file user.properties in the root directory. The file's format: 
            |"github-token=<your-token>."""".trimMargin(), valueSourceKey = "github-token")
                .prompt("GitHub Authorization Token")

fun CliktCommand.pairingPartner(nr: String) =
        option("-pp$nr", "--pairing-partner-$nr")
                .enum<PairingPartner>()
                .prompt()
