package ut.pullrequestfactory.domain.branches

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.Sessions

class SessionsTest {

    @Test
    fun for_branches_of_the_same_iteration_with_different_pairing_partner_the_session_number_gets_incremented() {
        val sessions = Sessions.createSessionsFor(listOf(
                branch_with(iterationNr = 1, pairingPartner = 1),
                branch_with(iterationNr = 1, pairingPartner = 2)))

        assertThat(sessions).isEqualTo(listOf("1", "2"))
    }

    @Test
    fun for_branches_of_two_iterations_with_the_same_pairing_partner_the_session_number_stays_the_same() {
        val sessions = Sessions.createSessionsFor(listOf(
                branch_with(iterationNr = 1, pairingPartner = 1),
                branch_with(iterationNr = 2, pairingPartner = 1)))

        assertThat(sessions).isEqualTo(listOf("1", "1"))
    }

    private fun branch_with(iterationNr: Int, pairingPartner: Int) =
            Branch("firstname_lastname_iteration_${iterationNr}_pairingpartner$pairingPartner")

}
