package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.Sessions

class SessionsTest {

    @Test
    fun for_branches_of_the_same_iteration_with_different_pairing_partner_the_session_number_gets_incremented() {
        val sut = Sessions(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_1_pairingpartner2")))

        val sessions = sut.create()

        assertThat(sessions).isEqualTo(listOf("1", "2"))
    }

    @Test
    fun for_branches_of_two_iterations_with_the_same_pairing_partner_the_session_number_stays_the_same() {
        val sut = Sessions(listOf(
                Branch("firstname_lastname_iteration_1_pairingpartner1"),
                Branch("firstname_lastname_iteration_2_pairingpartner1")))

        val sessions = sut.create()

        assertThat(sessions).isEqualTo(listOf("1", "1"))
    }

}
