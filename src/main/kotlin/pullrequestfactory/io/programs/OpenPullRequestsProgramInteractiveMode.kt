package pullrequestfactory.io.programs

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo

class OpenPullRequestsProgramInteractiveMode(
        private val ui: UI,
        private val repoUrl: String) : Program {

    override fun execute() {
        ui.show("Welcome to interactive mode for opening pull requests")
        ui.show("Please provide data for the following questions")
        val candidate = create_candidate()
        val token = ui.get_user_input(msg = "Your Github.com basic authorization token: ")
        val pairingPartner = create_pairing_partner()
        open_pull_requests_for(candidate, token, pairingPartner)
    }

    private fun create_candidate(): Candidate {
        val firstName = ui.get_user_input(msg = "Candidate first name: ")
        val lastName = ui.get_user_input(msg = "Candidate last name: ")
        val candidate = Candidate(firstName, lastName)
        return candidate
    }

    private fun create_pairing_partner(): List<PairingPartner> {
        val pairingPartner = (1..7).map {
            ui.show("${PairingPartner.values().toList()}")
            val pp = pairing_partner_for_session(it)
            pp
        }
        return pairingPartner
    }

    private fun pairing_partner_for_session(session: Int): PairingPartner {
        var pp: PairingPartner? = null
        while (pp == null) {
            try {
                val ppCandidate = ui.get_user_input(msg = "Pairing Partner Session $session: ")
                pp = PairingPartner.value_of(ppCandidate)
            } catch (e: Exception) {
                ui.show("Pairing partner name not supported. Please retry.")
            }
        }
        return pp
    }

    private fun open_pull_requests_for(candidate: Candidate, githubBasicAuthToken: String, pairingPartner: List<PairingPartner>) {
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui)
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, githubBasicAuthToken, ui)
        val f = GithubPRFactory(
                ui,
                branchesRepo,
                prRepo,
                BranchSyntaxValidator(ui),
                PullRequestLastNotFinishedMarker())
        ui.show("Open pull requests for: $candidate and $pairingPartner")
        f.open_pull_requests(candidate, pairingPartner)
    }

}
