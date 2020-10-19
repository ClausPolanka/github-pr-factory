package pullrequestfactory.io.uis

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.uis.UI

class PairingPartnerUITest {

    val pairingPartnerFromIndices = mutableListOf(
            index_from_user_input_for(PairingPartner.ANDREJ),
            index_from_user_input_for(PairingPartner.SHUBHI),
            index_from_user_input_for(PairingPartner.CLAUS),
            index_from_user_input_for(PairingPartner.BERNI),
            index_from_user_input_for(PairingPartner.DOMINIK),
            index_from_user_input_for(PairingPartner.MIHAI),
            index_from_user_input_for(PairingPartner.MICHAL))

    private val expectedPairingPartner = listOf(
            PairingPartner.ANDREJ,
            PairingPartner.SHUBHI,
            PairingPartner.CLAUS,
            PairingPartner.BERNI,
            PairingPartner.DOMINIK,
            PairingPartner.MIHAI,
            PairingPartner.MICHAL)

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

    @Test
    fun create_pairing_partner_from_user_input_for_invalid_index_which_is_not_a_number() {
        pairingPartnerFromIndices.add(0, "nanIndex")
        var actualMsg = "Expected one index not to be a number"

        val sut = PairingPartnerUI(object : UI {
            override fun show(msg: String) {
                actualMsg = if (msg.contains("nanIndex")) msg else actualMsg
            }

            override fun get_user_input(msg: String): String {
                val pp = pairingPartnerFromIndices[currentIdx]
                currentIdx = currentIdx.inc()
                return pp
            }
        })

        val actualPairingPartner = sut.create_pairing_partner_from_user_input()

        assertThat(actualMsg).isEqualTo("🤭 No pairing partner found for given index: 'nanIndex'")
        assertThat(actualPairingPartner).isEqualTo(expectedPairingPartner)
    }

    @Test
    fun create_pairing_partner_from_user_input_for_invalid_index_which_is_higher_than_number_of_supported_pairing_partner() {
        val tooHighIndex = PairingPartner.values().size.plus(1).toString()
        pairingPartnerFromIndices.add(0, tooHighIndex)
        var actualMsg = "Expected one index to be higher than number of supported pairing partner"

        val sut = PairingPartnerUI(object : UI {
            override fun show(msg: String) {
                actualMsg = if (msg.contains(tooHighIndex)) msg else actualMsg
            }

            override fun get_user_input(msg: String): String {
                val pp = pairingPartnerFromIndices[currentIdx]
                currentIdx = currentIdx.inc()
                return pp
            }
        })

        val actualPairingPartner = sut.create_pairing_partner_from_user_input()

        assertThat(actualMsg).isEqualTo("🤭 No pairing partner found for given index: '$tooHighIndex'")
        assertThat(actualPairingPartner).isEqualTo(expectedPairingPartner)
    }

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

    private fun index_from_user_input_for(pp: PairingPartner) =
            pp.ordinal.plus(1).toString()
}
