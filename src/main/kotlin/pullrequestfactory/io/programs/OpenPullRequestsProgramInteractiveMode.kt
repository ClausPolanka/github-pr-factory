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
        val pps = mutableListOf<String>()
        val pairingPartner = (1..7).map {
            ui.show(PairingPartner.indexed_names().toString())
            val pp = pairing_partner_for_session(it)
            pps.add("Pairing Partner Session $it: '${pp.pull_request_name()}'")
            ui.show(pps.joinToString(System.lineSeparator()))
            pp
        }
        return pairingPartner
    }

    private fun pairing_partner_for_session(session: Int): PairingPartner {
        var ppIdx = get_pairing_partner_idx_for(session)
        var ppOrdinal = ppIdx - 1
        var pp: PairingPartner? = null
        while (pp == null) {
            try {
                pp = PairingPartner.value_of(ppOrdinal)
            } catch (e: Exception) {
                ui.show("No pairing partner found for given index: '$ppIdx'")
                ppIdx = get_pairing_partner_idx_for(session)
                ppOrdinal = ppIdx - 1
            }
        }
        return pp
    }

    private fun get_pairing_partner_idx_for(session: Int): Int {
        var ppIdx = -1
        var ppIdxCandidate: String? = null
        while (ppIdx == -1) {
            try {
                ppIdxCandidate = ui.get_user_input(msg = "Pairing Partner Session $session: ")
                ppIdx = ppIdxCandidate.toInt()
            } catch (e: NumberFormatException) {
                ui.show("No pairing partner found for given index: '$ppIdxCandidate'")
            }
        }
        return ppIdx
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
