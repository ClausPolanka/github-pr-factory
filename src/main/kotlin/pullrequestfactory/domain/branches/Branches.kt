package pullrequestfactory.domain.branches

import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Sessions.createSessionsFor
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.pullrequests.PullRequestMarker

class Branches(
    private val branches: List<Branch>,
    private val prMarker: PullRequestMarker
) {

    fun getPullRequestsFor(pairingPartner: List<PairingPartner>): List<PullRequest> {
        val brs = sortBranchesBy(pairingPartner)
        with(brs) {
            val prs = branches.mapIndexed { idx, branch ->
                PullRequest(
                    title = getBranchTitles()[idx],
                    base = getBaseBranches()[idx].copy(),
                    head = branch.copy()
                )
            }
            return prMarker.markTitlesOf(prs)
        }
    }

    private fun sortBranchesBy(pairingPartner: List<PairingPartner>): Branches {
        val allIdxBranchPairs = mutableListOf<Pair<Int, Branch>>()
        val sortedByIterNr = branches.sortedBy { it.iterationNr() }.toMutableList()
        pairingPartner.forEach { pp ->
            for (iterationNr in 1..9) {
                val idxBranchPairs = sortedByIterNr
                    .mapIndexed { idx, br -> Pair(idx, br) }
                    .filter { it.second.iterationNr() == iterationNr }
                    .filter { pp.contains(it.second.pairingPartner()) }
                allIdxBranchPairs.addAll(idxBranchPairs)
            }
        }
        sortByIdx(allIdxBranchPairs, pairingPartner)
        val brs = allIdxBranchPairs.map { it.second }
        return Branches(brs, prMarker)
    }

    /**
     * Example
     * Before
     * idx, iter nr, pp-branch-name, pp
     * 0, 1, tomas, TOMAS
     * 2, 1, tomasr, TOMAS
     * 1, 1, claus, CLAUS
     * After
     * 0, 1, tomas, TOMAS
     * 1, 1, claus, CLAUS
     * 2, 1, tomasr, TOMAS
     */
    private fun sortByIdx(
        idxBranchPairs: MutableList<Pair<Int, Branch>>,
        pairingPartner: List<PairingPartner>
    ) {
        val aPairingPartnerHasMultipleSessions = pairingPartner
            .groupingBy { it }
            .eachCount()
            .any { it.value > 1 }
        when {
            aPairingPartnerHasMultipleSessions -> idxBranchPairs.sortBy { it.first }
        }
    }

    private fun getBranchTitles(): List<String> = branches.mapIndexed { idx, br ->
        val (firstName, lastName, _, iterationNr, pairingPartner) = br.parts()
        val titleParts = listOf(
            firstName.replaceFirstChar { it.titlecase() },
            lastName.replaceFirstChar { it.titlecase() },
            "Iteration",
            iterationNr,
            "/",
            "Session",
            createSessionsFor(branches)[idx],
            PairingPartner.from(pairingPartner)?.pullRequestName()
        )
        titleParts.joinToString(" ")
    }

    private fun getBaseBranches(): List<Branch> = branches.mapIndexed { idx, _ ->
        when (idx) {
            0 -> Branch("master")
            else -> branches[idx.dec()].copy()
        }
    }

}
