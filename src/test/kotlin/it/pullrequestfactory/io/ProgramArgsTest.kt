package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.ProgramArgs

private const val githubBasicAuthToken = "asdfkk3282kas8ölash8"

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
    fun throws_when_candidate_arguments_are_missing_a_candidate() {
        val sut = ProgramArgs(arrayOf("-c"))

        assertThatThrownBy { sut.get_candidate() }
    }

    @Test
    fun throws_when_candidate_arguments_are_missing_a_candidate_in_correct_format() {
        val sut = ProgramArgs(arrayOf("-c", "firstname"))

        assertThatThrownBy { sut.get_candidate() }
    }

    @Test
    fun throws_when_candidate_arguments_are_in_wrong_order() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "-c"))

        assertThatThrownBy { sut.get_candidate() }
    }

    @Test
    fun gets_github_basic_auth_token_for_correct_github_basic_auth_arguments() {
        val sut = ProgramArgs(arrayOf("-g", githubBasicAuthToken))

        val token = sut.get_github_basic_auth_token()

        assertThat(token).isEqualTo(githubBasicAuthToken)
    }

    @Test
    fun throws_when_github_basic_auth_token_arguments_are_missing_the_github_basic_auth_token_option() {
        val sut = ProgramArgs(arrayOf("-x", githubBasicAuthToken))

        assertThatThrownBy { sut.get_github_basic_auth_token() }
    }

    @Test
    fun throws_when_github_basic_auth_token_arguments_are_missing_the_github_basic_auth_token() {
        val sut = ProgramArgs(arrayOf("-g"))

        assertThatThrownBy { sut.get_github_basic_auth_token() }
    }

    @Test
    fun throws_when_github_basic_auth_token_arguments_are_in_wrong_order() {
        val sut = ProgramArgs(arrayOf(githubBasicAuthToken, "-g"))

        assertThatThrownBy { sut.get_github_basic_auth_token() }
    }

}
