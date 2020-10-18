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
        ui.show("👋 Welcome to interactive mode for opening pull requests")
        ui.show("Please provide data for the following questions")
        val candidate = create_candidate_from_user_input()
        val token = create_basic_auth_token_from_user_input()
        val pp = create_pairing_partner_from_user_input()
        open_pull_requests_for(candidate, token, pp)
    }

    private fun create_candidate_from_user_input(): Candidate {
        val firstName = ui.get_user_input(msg = "👉 Candidate first name: ")
        val lastName = ui.get_user_input(msg = "👉 Candidate last name: ")
        val c = Candidate(firstName, lastName)
        return c
    }

    private fun create_basic_auth_token_from_user_input(): String {
        val token = ui.get_user_input(msg = "👉 Your Github.com basic authorization token: ")
        return token
    }

    private fun create_pairing_partner_from_user_input(): List<PairingPartner> {
        val chosenPPs = mutableListOf<String>()
        val pps = (1..7).map {
            ui.show(PairingPartner.indexed_names().toString())
            val pp = pairing_partner_for_session(it)
            chosenPPs.add("👌 Pairing Partner Session $it: '${pp.pull_request_name()}'")
            ui.show(chosenPPs.joinToString(System.lineSeparator()))
            pp
        }
        return pps
    }

    private fun pairing_partner_for_session(session: Int): PairingPartner {
        var ppIdx = get_pairing_partner_idx_for(session)
        var ppOrdinal = ppIdx - 1
        var pp: PairingPartner? = null
        while (pp == null) {
            try {
                pp = PairingPartner.value_of(ppOrdinal)
            } catch (e: Exception) {
                ui.show("🤭 No pairing partner found for given index: '$ppIdx'")
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
                ppIdxCandidate = ui.get_user_input(msg = "👉 Pairing Partner Session $session: ")
                ppIdx = ppIdxCandidate.toInt()
            } catch (e: NumberFormatException) {
                ui.show("🤭 No pairing partner found for given index: '$ppIdxCandidate'")
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
        ui.show("🤩 Open pull requests for: $candidate and $pairingPartner")
        f.open_pull_requests(candidate, pairingPartner)
    }

}
