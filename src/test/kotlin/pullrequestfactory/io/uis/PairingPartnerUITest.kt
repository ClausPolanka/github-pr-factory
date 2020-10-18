package pullrequestfactory.io.uis

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.uis.UI

class PairingPartnerUITest {

    private var currentIdx = 0

    @Test
    fun create_pairing_partner_from_user_input_which_is_a_list_of_indices() {
        val sut = PairingPartnerUI(test_ui_for(listOf(
                index_from_user_input_for(PairingPartner.ANDREJ),
                index_from_user_input_for(PairingPartner.SHUBHI),
                index_from_user_input_for(PairingPartner.CLAUS),
                index_from_user_input_for(PairingPartner.BERNI),
                index_from_user_input_for(PairingPartner.DOMINIK),
                index_from_user_input_for(PairingPartner.MIHAI),
                index_from_user_input_for(PairingPartner.MICHAL))))

        val pps = sut.create_pairing_partner_from_user_input()

        assertThat(pps).isEqualTo(listOf(
                PairingPartner.ANDREJ,
                PairingPartner.SHUBHI,
                PairingPartner.CLAUS,
                PairingPartner.BERNI,
                PairingPartner.DOMINIK,
                PairingPartner.MIHAI,
                PairingPartner.MICHAL))
    }

    private fun index_from_user_input_for(pp: PairingPartner) =
            pp.ordinal.plus(1).toString()

    private fun test_ui_for(pps: List<String>): UI {
        return object : UI {
            override fun show(msg: String) {
            }

            override fun get_user_input(msg: String): String {
                val pp = pps[currentIdx]
                currentIdx = currentIdx.inc()
                return pp
            }
        }
    }
}
