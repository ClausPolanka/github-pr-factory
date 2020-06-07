package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.Sessions

class SessionsTest {

    @Test
    fun create_sessions_for_two_branches_for_same_iterations_but_different_pairing_partner() {
        val sut = Sessions(listOf(
                Branch("fn_ln_iteration_1_pp1"),
                Branch("fn_ln_iteration_1_pp2")))

        val sessions = sut.create()

        assertThat(sessions).isEqualTo(listOf("1", "2"))
    }

    @Test
    fun create_sessions_for_two_branches_for_different_iterations_for_same_pairing_partner() {
        val sut = Sessions(listOf(
                Branch("fn_ln_iteration_1_pp1"),
                Branch("fn_ln_iteration_2_pp1")))

        val sessions = sut.create()

        assertThat(sessions).isEqualTo(listOf("1", "1"))
    }
}
