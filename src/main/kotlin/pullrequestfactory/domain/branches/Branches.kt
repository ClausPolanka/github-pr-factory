package pullrequestfactory.domain.branches

import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.pullrequests.PullRequestMarker

class Branches(private val branches: List<Branch>, private val pullRequestMarker: PullRequestMarker) {

    val base_branches: List<Branch>
        get() = branches.mapIndexed { idx, _ ->
            when (idx) {
                0 -> Branch("master")
                else -> branches[idx.dec()]
            }
        }

    val branch_titles: List<String>
        get() = branches.mapIndexed { idx, br ->
            val (firstName, lastName, _, iterationNr, pairingPartner) = br.parts()
            "${firstName.capitalize()} ${lastName.capitalize()} " +
                    "Iteration $iterationNr / Session ${branch_sessions[idx]} ${pairingPartner.capitalize()}"
        }

    private val branch_sessions: List<String>
        get() = Sessions.create_sessions_for(branches)

    fun pull_requests_for(pairingPartner: List<String>): List<PullRequest> {
        val brs = sort_branches_by(pairingPartner)
        with(brs) {
            val pullRequests = branches.mapIndexed { idx, branch ->
                PullRequest(
                        title = branch_titles[idx],
                        _base = base_branches[idx],
                        _head = branch)
            }
            return pullRequestMarker.mark_titles_of(pullRequests)
        }
    }

    private fun sort_branches_by(pairingPartner: List<String>) = Branches(
            pairingPartner.map { sort_branches_by(it) }.flatten(),
            pullRequestMarker)

    private fun sort_branches_by(pairingPartner: String) =
            branches.filter { it.name.endsWith(pairingPartner.toLowerCase()) }
                    .map { Pair(it.name, it.iteration_nr()) }
                    .sortedBy { it.second }
                    .map { Branch(it.first) }

}
