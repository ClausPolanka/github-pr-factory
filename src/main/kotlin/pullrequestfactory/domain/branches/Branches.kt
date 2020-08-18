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

    fun get_pull_requests_for(pairingPartner: List<PairingPartner>): List<PullRequest> {
        val brs = sort_branches_by(pairingPartner)
        with(brs) {
            val pullRequests = branches.mapIndexed { idx, branch ->
                PullRequest(
                        title = branch_titles[idx],
                        _base = base_branches[idx].copy(),
                        _head = branch.copy())
            }
            val prs = pullRequestMarker.mark_titles_of(pullRequests)
            return prs
        }
    }

    private fun sort_branches_by(pairingPartner: List<PairingPartner>): Branches {
        val sorted = pairingPartner
                .map { pp ->
                    val brs = sort_branches_by(pp)
                    brs
                }
                .flatten()
        val sortedBranches = Branches(
                sorted,
                pullRequestMarker)
        return sortedBranches
    }

    private fun sort_branches_by(pairingPartner: PairingPartner): List<Branch> {
        val sortedBranches = branches
                .filter { br ->
                    val pp = br.pairing_partner()
                    pairingPartner.contains(pp) // TOMAS contains tomas and tomasr
                }
                .map { br -> Pair(br.name, br.iteration_nr()) }
                .sortedBy { br -> br.second }
                .map { br -> Branch(br.first) }
        return sortedBranches
    }

}
