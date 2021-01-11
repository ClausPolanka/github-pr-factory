package pullrequestfactory.io.clikt

import pullrequestfactory.CloseMode
import pullrequestfactory.CloseModeInteractive
import pullrequestfactory.CloseModeRegular
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class CliktClosePRsFactory(private val ui: ConsoleUI, private val repoUrl: String) {

    fun create_for(mode: CloseMode): CliktClosePrsProgram {
        return when (val it = mode) {
            is CloseModeInteractive -> {
                val httpClient = KhttpClientStats(KhttpClient(it.githubAuthorizationToken))
                val candidate = Candidate(it.candidateFirstName, it.candidateLastName)
                CliktClosePrsProgram(ui, repoUrl, httpClient, candidate)
            }
            is CloseModeRegular -> {
                val httpClient = KhttpClientStats(KhttpClient(it.githubAuthorizationToken))
                val candidate = Candidate(it.candidate.split("-")[0], it.candidate.split("-")[1])
                CliktClosePrsProgram(ui, repoUrl, httpClient, candidate)
            }
        }
    }

}
