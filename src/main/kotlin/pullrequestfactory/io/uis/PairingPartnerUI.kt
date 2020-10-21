package pullrequestfactory.io.uis

import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.uis.UI

class PairingPartnerUI(private val ui: UI) {

    private val sessions = (1..7)
    private val chosenPPsUserOutput = mutableListOf<String>()

    fun create_pairing_partner_from_user_input(): List<PairingPartner> {
        val pps = sessions.map { session ->
            ui.show(PairingPartner.indexed_names().toString())
            val pp = pairing_partner_for_session(session)
            ui.show(create_user_output_for(session, pp))
            pp
        }
        return pps
    }

    private fun pairing_partner_for_session(session: Int): PairingPartner {
        var ppIdx = get_pairing_partner_idx_for(session)
        var ppOrdinal = ppIdx - 1
        var pp = PairingPartner.value_of(ppOrdinal)
        while (pp == null) {
            ui.show("🤭 No pairing partner found for given index: '$ppIdx'")
            ppIdx = get_pairing_partner_idx_for(session)
            ppOrdinal = ppIdx - 1
            pp = PairingPartner.value_of(ppOrdinal)
        }
        return pp
    }

    private fun get_pairing_partner_idx_for(session: Int): Int {
        var ppIdx: Int? = null
        while (ppIdx == null) {
            val ppIdxCandidate = ui.get_user_input(msg = "👉 Pairing Partner Session $session: ")
            ppIdx = to_int(ppIdxCandidate)
        }
        return ppIdx
    }

    private fun to_int(ppIdxCandidate: String): Int? {
        try {
            return ppIdxCandidate.toInt()
        } catch (e: NumberFormatException) {
            ui.show("🤭 No pairing partner found for given index: '$ppIdxCandidate'")
        }
        return null
    }

    private fun create_user_output_for(session: Int, pp: PairingPartner): String {
        chosenPPsUserOutput.add("👌 Pairing Partner Session $session: '${pp.pull_request_name()}'")
        val userOutput = chosenPPsUserOutput.joinToString(System.lineSeparator())
        return userOutput
    }

}
