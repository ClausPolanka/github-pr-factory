package pullrequestfactory.domain.branches

/**
 * A session represents the unit of an hour of pair-programming between
 * the candidate and and an existing team-member. A new pairing-partner always
 * results in a new session.
 */
internal object Sessions {

    fun createSessionsFor(branches: List<Branch>): List<String> {
        var prevSession = 1
        return branches.mapIndexed { idx, branch ->
            when (idx) {
                0 -> prevSession.toString()
                else -> {
                    val prevPairingPartner = branches[idx - 1].pairingPartner()
                    val currPairingPartner = branch.pairingPartner()
                    if (prevPairingPartner == currPairingPartner) {
                        "$prevSession"
                    } else {
                        prevSession = prevSession.inc()
                        "$prevSession"
                    }
                }
            }
        }
    }

}

