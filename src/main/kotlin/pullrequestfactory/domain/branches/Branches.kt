package pullrequestfactory.domain.branches

import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Sessions.create_sessions_for
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.pullrequests.PullRequestMarker

class Branches(private val branches: List<Branch>, private val pullRequestMarker: PullRequestMarker) {

    val base_branches: List<Branch>
        get() = branches.mapIndexed { idx, _ ->
            when (idx) {
                0 -> Branch("master")
                else -> branches[idx.dec()].copy()
            }
        }

    val branch_titles: List<String>
        get() = branches.mapIndexed { idx, br ->
            val (firstName, lastName, _, iterationNr, pairingPartner) = br.parts()
            val titleParts = listOf(
                    firstName.capitalize(),
                    lastName.capitalize(),
                    "Iteration",
                    iterationNr,
                    "/",
                    "Session",
                    create_sessions_for(branches)[idx],
                    pairingPartner.capitalize()
            )
            titleParts.joinToString(" ")
        }

    fun pull_requests_for(pairingPartner: List<PairingPartner>): List<PullRequest> {
        val brs = sort_branches_by(pairingPartner)
        with(brs) {
            val pullRequests = branches.mapIndexed { idx, branch ->
                PullRequest(
                        title = branch_titles[idx],
                        _base = base_branches[idx].copy(),
                        _head = branch.copy())
            }
            return pullRequestMarker.mark_titles_of(pullRequests)
        }
    }

    private fun sort_branches_by(pairingPartner: List<PairingPartner>) = Branches(
            pairingPartner.map { sort_branches_by(it) }.flatten(),
            pullRequestMarker)

    private fun sort_branches_by(pairingPartner: PairingPartner) =
            branches.filter { it.name.endsWith(pairingPartner.name.toLowerCase()) }
                    .map { Pair(it.name, it.iteration_nr()) }
                    .sortedBy { it.second }
                    .map { Branch(it.first) }

}
