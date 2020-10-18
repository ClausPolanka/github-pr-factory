package pullrequestfactory.io.uis

import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.uis.UI

class PairingPartnerUI(private val ui: UI) {

    private val sessions = (1..7)

    fun create_pairing_partner_from_user_input(): List<PairingPartner> {
        val chosenPPs = mutableListOf<String>()
        val pps = sessions.map { session ->
            ui.show(PairingPartner.indexed_names().toString())
            val pp = pairing_partner_for_session(session)
            chosenPPs.add("👌 Pairing Partner Session $session: '${pp.pull_request_name()}'")
            ui.show(chosenPPs.joinToString(System.lineSeparator()))
            pp
        }
        return pps
    }

    private fun pairing_partner_for_session(session: Int): PairingPartner {
        var ppIdx = get_pairing_partner_idx_for(session)
        var ppOrdinal = ppIdx - 1
        var pp: PairingPartner? = null
        while (pp == null) {
            try {
                pp = PairingPartner.value_of(ppOrdinal)
            } catch (e: Exception) {
                ui.show("🤭 No pairing partner found for given index: '$ppIdx'")
                ppIdx = get_pairing_partner_idx_for(session)
                ppOrdinal = ppIdx - 1
            }
        }
        return pp
    }

    private fun get_pairing_partner_idx_for(session: Int): Int {
        var ppIdx = Int.MIN_VALUE
        while (ppIdx == Int.MIN_VALUE) {
            val ppIdxCandidate = ui.get_user_input(msg = "👉 Pairing Partner Session $session: ")
            ppIdx = to_int(ppIdxCandidate)
        }
        return ppIdx
    }

    private fun to_int(ppIdxCandidate: String): Int {
        try {
            return ppIdxCandidate.toInt()
        } catch (e: NumberFormatException) {
            ui.show("🤭 No pairing partner found for given index: '$ppIdxCandidate'")
        }
        return Int.MIN_VALUE
    }

}
