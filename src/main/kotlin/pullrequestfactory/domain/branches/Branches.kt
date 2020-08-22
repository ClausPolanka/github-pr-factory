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
                    PairingPartner.value_of(pairingPartner).pull_request_name()
            )
            titleParts.joinToString(" ")
        }

    fun get_pull_requests_for(pairingPartner: List<PairingPartner>): List<PullRequest> {
        val brs = sort_branches_by(pairingPartner)
        with(brs) {
            val pullRequests = branches.mapIndexed { idx, branch ->
                PullRequest(
                        title = brs.branch_titles[idx],
                        _base = base_branches[idx].copy(),
                        _head = branch.copy())
            }
            val prs = pullRequestMarker.mark_titles_of(pullRequests)
            return prs
        }
    }

    private fun sort_branches_by(pairingPartner: List<PairingPartner>): Branches {
        val sorted = mutableListOf<Branch>()
        val sortedByIterNr = branches.sortedBy { it.iteration_nr() }.toMutableList()
        pairingPartner.forEach { pp ->
            for (i in 1..7) {
                val filtered = sortedByIterNr.filter { it.iteration_nr() == i }
                        .filter { pp.contains(it.pairing_partner()) }
                sorted.addAll(filtered)
                sortedByIterNr.removeAll(filtered)
            }
        }
        return Branches(sorted, pullRequestMarker)
    }

}
