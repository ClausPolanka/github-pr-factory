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
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.io.programs.ProgramArgs
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.programs.impl.OpenPRsProgramLastSessionNotFinished
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.repositories.KhttpClient
import pullrequestfactory.io.repositories.KhttpClientStats
import pullrequestfactory.io.uis.ConsoleUI

class `Github-Pr-Factory` : CliktCommand() {
    override fun run() = Unit
}

sealed class OpenMode(name: String) : OptionGroup(name)

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
    val isLastIterationFinished by option(help = "Set if last iteration is finished", names = arrayOf("-l", "--last-finished")).flag()
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
        when (val it = mode) {
            is OpenModeInteractive -> {
                echo("Opening Pull Requests for ${it.candidateFirstName} ${it.candidateLastName}")
                val ui = ConsoleUI()
                val appProps = FileAppProperties("app.properties")
                val baseUrl = appProps.get_github_base_url()
                val repoPath = appProps.get_github_repository_path()
                val repoUrl = baseUrl + repoPath
                val program = OpenPRsProgramLastSessionNotFinished(ui,
                        repoUrl,
                        KhttpClientStats(KhttpClient(it.githubAuthorizationToken)),
                        Candidate(it.candidateFirstName, it.candidateLastName),
                        create_pairing_partner("${it.pairingPartner1}-${it.pairingPartner2}-${it.pairingPartner3}-${it.pairingPartner4}-${it.pairingPartner5}-${it.pairingPartner6}-${it.pairingPartner7}"))
                program.execute()
            }
            is OpenModeRegular -> {
                echo("Opening Pull Requests for ${Candidate(it.candidate!!.split("-")[0], it.candidate!!.split("-")[1])}")
                val ui = ConsoleUI()
                val appProps = FileAppProperties("app.properties")
                val baseUrl = appProps.get_github_base_url()
                val repoPath = appProps.get_github_repository_path()
                val repoUrl = baseUrl + repoPath
                val program = OpenPRsProgramLastSessionNotFinished(ui,
                        repoUrl,
                        KhttpClientStats(KhttpClient(it.githubToken!!)),
                        Candidate(it.candidate!!.split("-")[0], it.candidate!!.split("-")[1]),
                        create_pairing_partner(it.pairingPartner!!))
                program.execute()
            }
        }
    }
}
private val ERROR_MSG_PAIRING_PARTNER = "Either option -p or pairing partner are missing or pairing partner have wrong format or is unknown"
private fun create_pairing_partner(pairingPartner: String): List<PairingPartner> {
    return pairingPartner.split("-").map { br ->
        val pp = PairingPartner.value_of(br)
        when (pp) {
            null -> throw ProgramArgs.WrongPairingPartnerArgumentSyntax("$ERROR_MSG_PAIRING_PARTNER for given branch '$br'")
            else -> pp
        }
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
        when (val it = mode) {
            is CloseModeInteractive -> {
                close_pull_requests_interactively(it)
            }
            is CloseModeRegular -> {
                val ui = ConsoleUI()
                val appProps = FileAppProperties("app.properties")
                val baseUrl = appProps.get_github_base_url()
                val repoPath = appProps.get_github_repository_path()
                val repoUrl = baseUrl + repoPath
                val httpClient = KhttpClientStats(KhttpClient(it.githubAuthorizationToken))
                val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClient)
                val prRepo = GithubHttpPullRequestsRepo(repoUrl, ui, httpClient)
                val f = GithubPRFactory(
                        ConsoleUI(),
                        branchesRepo,
                        prRepo,
                        BranchSyntaxValidator(ui),
                        PullRequestLastNotFinishedMarker())
                f.close_pull_requests_for(Candidate(it.candidate.split("-")[0], it.candidate.split("-")[1]))
            }
        }
    }

    private fun close_pull_requests_interactively(closeMode: CloseModeInteractive) {
        val ui = ConsoleUI()
        val appProps = FileAppProperties("app.properties")
        val baseUrl = appProps.get_github_base_url()
        val repoPath = appProps.get_github_repository_path()
        val repoUrl = baseUrl + repoPath
        val httpClient = KhttpClient(closeMode.githubAuthorizationToken)
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, httpClient)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, ui, httpClient)
        val f = GithubPRFactory(
                ui,
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        f.close_pull_requests_for(Candidate(closeMode.candidateFirstName, closeMode.candidateLastName))
    }
}

fun main(args: Array<String>) = `Github-Pr-Factory`()
        .subcommands(Open(), Close())
        .main(args)

