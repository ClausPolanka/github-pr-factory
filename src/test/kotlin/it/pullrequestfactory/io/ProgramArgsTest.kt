package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.ProgramArgs

class ProgramArgsTest {

    @Test
    fun gets_candidate_for_correct_candidate_arguments() {
        val sut = ProgramArgs(arrayOf("-c", "firstname-lastname"))

        val candidate = sut.get_candidate()

        assertThat(candidate).isEqualTo(Candidate("firstname", "lastname"))
    }

    @Test
    fun throws_when_candidate_arguments_are_missing_the_candiate_option() {
        val sut = ProgramArgs(arrayOf("-x", "firstname-lastname"))

        assertThatThrownBy { sut.get_candidate() }
    }

    @Test
    fun throws_when_candidate_arguments_are_missing_a_candidate_in_correct_format() {
        val sut = ProgramArgs(arrayOf("-x", "firstname"))

        assertThatThrownBy { sut.get_candidate() }
    }

    @Test
    fun throws_when_candidate_arguments_are_in_wrong_order() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "-c"))

        assertThatThrownBy { sut.get_candidate() }
    }

}
