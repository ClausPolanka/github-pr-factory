package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.PairingPartner

class PairingPartnerTest {

    @Test
    fun pairing_partner_contains_given_branch_name() {
        val branchName = "berni"
        val contains = PairingPartner.BERNI.contains(branchName)

        assertThat(contains)
                .describedAs("expecting branch name to be contained: '$branchName'")
                .isTrue()
    }

    @Test
    fun returns_pull_request_name_for_given_pairing_partner() {
        val prName = PairingPartner.BERNI.pullRequestName()

        assertThat(prName).isEqualTo("Berni")
    }

    @Test
    fun pairing_partner_branch_names_for_definied_pairing_partner() {
        val ppName = "tomasr"
        val pp = PairingPartner.value_of(ppName)

        assertThat(pp)
                .describedAs("expecting known pairing partner name: '$ppName'")
                .isEqualTo(PairingPartner.TOMAS)
    }

    @Test
    fun pairing_partner_branch_names_for_definied_pairing_partner_berni() {
        val ppName = "berni"
        val pp = PairingPartner.value_of(ppName)

        assertThat(pp)
                .describedAs("expecting known pairing partner name: '$ppName∆'")
                .isEqualTo(PairingPartner.BERNI)
    }

    @Test
    fun pairing_partner_branch_names_for_definied_pairing_partner_shubi() {
        val ppName = "shubi"
        val pp = PairingPartner.value_of(ppName)

        assertThat(pp)
                .describedAs("expecting known pairing partner name: '$ppName'")
                .isEqualTo(PairingPartner.SHUBHI)
    }

    @Test
    fun returns_null_when_pairing_partner_name_in_branch_is_invalid() {
        val ppName = "xxx"
        val pp = PairingPartner.value_of(ppName)

        assertThat(pp)
                .describedAs("expecting pairing partner to be invalid: '$ppName'")
                .isNull()
    }

    @Test
    fun returns_null_when_when_ordinal_is_too_high() {
        val ordinal = PairingPartner.values().size
        val pp = PairingPartner.value_of(ordinal)

        assertThat(pp)
                .describedAs("ordinal is not high enough: '$ordinal'")
                .isNull()
    }

    @Test
    fun returns_pairing_partner_for_given_ordinal() {
        val ordinal = 0
        val pp = PairingPartner.value_of(ordinal)

        assertThat(pp)
                .describedAs("expecting valid ordinal: '$ordinal'")
                .isEqualTo(PairingPartner.ANDREJ)
    }

    @Test
    fun returns_indexed_pairing_partner_pull_request_names() {
        val pps = PairingPartner.indexedNames()

        assertThat(pps.size).isEqualTo(pps.size)
        assertThat(pps[0]).isEqualTo("Andrej (1)")
    }

}
