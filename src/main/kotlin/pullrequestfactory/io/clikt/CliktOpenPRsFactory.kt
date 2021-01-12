package pullrequestfactory.io.clikt

import com.github.ajalt.clikt.output.TermUi.echo
import pullrequestfactory.OpenMode
import pullrequestfactory.OpenModeInteractive
import pullrequestfactory.OpenModeRegular
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.io.GithubAPIClient
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.programs.impl.OpenPRsProgramLastSessionNotFinished
import pullrequestfactory.io.programs.impl.OpenPullRequestsProgram
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class CliktOpenPRsFactory(
        private val ui: ConsoleUI,
        private val baseUrl: String,
        private val repoPath: String
) {

    private val ERROR_MSG_PAIRING_PARTNER = "Either option -p or pairing partner are missing or pairing partner have wrong format or is unknown"

    fun create_for(mode: OpenMode): Program {
        return when (val it = mode) {
            is OpenModeInteractive -> {
                echo("Opening Pull Requests for ${it.candidateFirstName} ${it.candidateFirstName}")
                OpenPRsProgramLastSessionNotFinished(ui,
                        baseUrl + repoPath,
                        KhttpClientStats(KhttpClient(it.githubAuthorizationToken)),
                        Candidate(it.candidateFirstName, it.candidateLastName),
                        create_pairing_partner("${it.pairingPartner1}-${it.pairingPartner2}-${it.pairingPartner3}-${it.pairingPartner4}-${it.pairingPartner5}-${it.pairingPartner6}-${it.pairingPartner7}"))
            }
            is OpenModeRegular -> {
                val candidate = Candidate(it.candidate!!.split("-")[0], it.candidate!!.split("-")[1])
                echo("Opening Pull Requests for ${candidate.firstName} ${candidate.lastName}")
                val httpClient = KhttpClientStats(KhttpClient(it.githubToken!!))
                val pairingPartner = create_pairing_partner(it.pairingPartner!!)
                OpenPullRequestsProgram(ui,
                        baseUrl + repoPath,
                        GithubAPIClient(httpClient, baseUrl),
                        httpClient,
                        it.isLastIterationFinished,
                        candidate,
                        pairingPartner)
            }
        }
    }

    private fun create_pairing_partner(pairingPartner: String): List<PairingPartner> {
        return pairingPartner.split("-").map { br ->
            val pp = PairingPartner.value_of(br)
            when (pp) {
                null -> throw ProgramArgs.WrongPairingPartnerArgumentSyntax("$ERROR_MSG_PAIRING_PARTNER for given branch '$br'")
                else -> pp
            }
        }
    }

}
