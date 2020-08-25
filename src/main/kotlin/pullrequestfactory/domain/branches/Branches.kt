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
                        title = branch_titles[idx],
                        _base = base_branches[idx].copy(),
                        _head = branch.copy())
            }
            val prs = pullRequestMarker.mark_titles_of(pullRequests)
            return prs
        }
    }

    private fun sort_branches_by(pairingPartner: List<PairingPartner>): Branches {
        val allIdxBranchPairs = mutableListOf<Pair<Int, Branch>>()
        val sortedByIterNr = branches.sortedBy { it.iteration_nr() }.toMutableList()
        pairingPartner.forEach { pp ->
            for (iterationNr in 1..9) {
                val idxBranchPairs = sortedByIterNr
                        .mapIndexed { idx, br -> Pair(idx, br) }
                        .filter { it.second.iteration_nr() == iterationNr }
                        .filter { pp.contains(it.second.pairing_partner()) }
                allIdxBranchPairs.addAll(idxBranchPairs)
                sortedByIterNr.removeAll(idxBranchPairs.map { it.second })
            }
        }
        sort_by_idx(allIdxBranchPairs, pairingPartner)
        return Branches(allIdxBranchPairs.map { it.second }, pullRequestMarker)
    }

    private fun sort_by_idx(idxBranchPairs: MutableList<Pair<Int, Branch>>, pairingPartner: List<PairingPartner>) {
        val aPairingPartnerHasMultipleSessions = pairingPartner
                .groupingBy { it }
                .eachCount()
                .any { it.value > 1 }
        when {
            aPairingPartnerHasMultipleSessions -> idxBranchPairs.sortBy { it.first }
        }
    }

}
