package ut.pullrequestfactory.domain.branches

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.Sessions

class SessionsTest {

    @Test
    fun `for branches of the same iteration with different pairing partner the session number gets incremented`() {
        val sessions = Sessions.createSessionsFor(
            listOf(
                branchWith(iterationNr = 1, pairingPartner = 1),
                branchWith(iterationNr = 1, pairingPartner = 2)
            )
        )

        assertThat(sessions).isEqualTo(listOf("1", "2"))
    }

    @Test
    fun `for branches of two iterations with the same pairing partner the session number stays the same`() {
        val sessions = Sessions.createSessionsFor(
            listOf(
                branchWith(iterationNr = 1, pairingPartner = 1),
                branchWith(iterationNr = 2, pairingPartner = 1)
            )
        )

        assertThat(sessions).isEqualTo(listOf("1", "1"))
    }

    private fun branchWith(iterationNr: Int, pairingPartner: Int) =
        Branch("firstname_lastname_iteration_${iterationNr}_pairingpartner$pairingPartner")

}
