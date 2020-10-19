package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.PairingPartner

class PairingPartnerTest {

    @Test
    fun pairing_partner_branch_names_for_definied_pairing_partner() {
        val pp = PairingPartner.value_of("tomasr")

        assertThat(pp).isEqualTo(PairingPartner.TOMAS)
    }

    @Test
    fun pairing_partner_branch_names_for_definied_pairing_partner_berni() {
        val pp = PairingPartner.value_of("berni")

        assertThat(pp).isEqualTo(PairingPartner.BERNI)
    }

    @Test
    fun pairing_partner_branch_names_for_definied_pairing_partner_shubi() {
        val pp = PairingPartner.value_of("shubi")

        assertThat(pp).isEqualTo(PairingPartner.SHUBHI)
    }

    @Test
    fun returns_null_when_pairing_partner_name_in_branch_is_invalid() {
        val pp = PairingPartner.value_of("xxx")

        assertThat(pp).isNull()
    }

}
