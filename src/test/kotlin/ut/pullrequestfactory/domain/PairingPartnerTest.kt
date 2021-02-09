package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.PairingPartner

class PairingPartnerTest {

    @Test
    fun `pairing partner contains given branch name`() {
        val branchName = "berni"
        val contains = PairingPartner.BERNI.contains(branchName)

        assertThat(contains)
            .describedAs("expecting branch name to be contained: '$branchName'")
            .isTrue
    }

    @Test
    fun `returns pull request name for given pairing partner`() {
        val prName = PairingPartner.BERNI.pullRequestName()

        assertThat(prName).isEqualTo("Berni")
    }

    @Test
    fun `pairing partner branch names for definied pairing partner`() {
        val ppName = "tomasr"
        val pp = PairingPartner.from(ppName)

        assertThat(pp)
            .describedAs("expecting known pairing partner name: '$ppName'")
            .isEqualTo(PairingPartner.TOMAS)
    }

    @Test
    fun `pairing partner branch names for definied pairing partner berni`() {
        val ppName = "berni"
        val pp = PairingPartner.from(ppName)

        assertThat(pp)
            .describedAs("expecting known pairing partner name: '$ppName∆'")
            .isEqualTo(PairingPartner.BERNI)
    }

    @Test
    fun `pairing partner branch names for definied pairing partner shubi`() {
        val ppName = "shubi"
        val pp = PairingPartner.from(ppName)

        assertThat(pp)
            .describedAs("expecting known pairing partner name: '$ppName'")
            .isEqualTo(PairingPartner.SHUBHI)
    }

    @Test
    fun `returns null when pairing partner name in branch is invalid`() {
        val ppName = "xxx"
        val pp = PairingPartner.from(ppName)

        assertThat(pp)
            .describedAs("expecting pairing partner to be invalid: '$ppName'")
            .isNull()
    }

    @Test
    fun `returns null when when ordinal is too high`() {
        val ordinal = PairingPartner.values().size
        val pp = PairingPartner.from(ordinal)

        assertThat(pp)
            .describedAs("ordinal is not high enough: '$ordinal'")
            .isNull()
    }

    @Test
    fun `returns pairing partner for given ordinal`() {
        val ordinal = 0
        val pp = PairingPartner.from(ordinal)

        assertThat(pp)
            .describedAs("expecting valid ordinal: '$ordinal'")
            .isEqualTo(PairingPartner.ANDREJ)
    }

    @Test
    fun `returns indexed pairing partner pull request names`() {
        val pps = PairingPartner.indexedNames()

        assertThat(pps.size).isEqualTo(pps.size)
        assertThat(pps[0]).isEqualTo("Andrej (1)")
    }

}
