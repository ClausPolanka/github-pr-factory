package pullrequestfactory.domain

/**
 * A session represents the unit of an hour of pair-programming between
 * the candidate and and an existing team-member.
 */
class Sessions(private val branches: List<Branch>) {

    fun create(): List<String> {
        var prevSession = 1
        return branches.mapIndexed { idx, branch ->
            when (idx) {
                0 -> prevSession.toString()
                else -> {
                    val currPairingPartner = branch.pairing_partner()
                    val prevPairingPartner = branches[idx - 1].pairing_partner()
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

