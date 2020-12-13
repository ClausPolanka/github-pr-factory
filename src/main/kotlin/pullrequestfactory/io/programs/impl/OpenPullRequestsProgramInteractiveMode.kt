package pullrequestfactory.io.programs.impl

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.repositories.GithubHttpBranchesRepos
import pullrequestfactory.io.repositories.GithubHttpPullRequestsRepo
import pullrequestfactory.io.uis.PairingPartnerUI

class OpenPullRequestsProgramInteractiveMode(
        private val ui: UI,
        private val repoUrl: String,
        private val authToken: String? = null) : Program {

    private val ppUI = PairingPartnerUI(ui)

    override fun execute() {
        show_welcome_message()
        val candidate = create_candidate_from_user_input()
        val token = create_auth_token_from_user_input()
        val pp = ppUI.create_pairing_partner_from_user_input()
        open_pull_requests_for(candidate, token, pp)
    }

    private fun show_welcome_message() {
        ui.show("👋 Welcome to interactive mode for opening pull requests")
        ui.show("Please provide data for the following questions")
    }

    private fun create_candidate_from_user_input(): Candidate {
        val firstName = ui.get_user_input(msg = "👉 Candidate first name: ")
        val lastName = ui.get_user_input(msg = "👉 Candidate last name: ")
        val c = Candidate(firstName, lastName)
        return c
    }

    private fun create_auth_token_from_user_input(): String {
        if (authToken != null) {
            return authToken
        }
        val token = ui.get_user_input(msg = "👉 Your Github.com authorization token: ")
        return token
    }

    private fun open_pull_requests_for(candidate: Candidate, githubAuthToken: String, pairingPartner: List<PairingPartner>) {
        val branchesRepo = GithubHttpBranchesRepos(repoUrl, ui, create_auth_token_from_user_input())
        val prRepo = GithubHttpPullRequestsRepo(repoUrl, githubAuthToken, ui)
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
