package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.io.ProgramArgs

private const val githubBasicAuthToken = "asdfkk3282kas8ölash8"
private const val pairingPartner = "claus-berni-dominik-andrej-shubi-tibor-nandor"

class ProgramArgsTest {

    @Test
    fun has_help_option_for_empty_args() {
        val sut = ProgramArgs(emptyArray())

        val hasHelpOption = sut.has_help_option()

        assertThat(hasHelpOption).isTrue()
    }

    @Test
    fun has_help_option_for_help_option() {
        val sut = ProgramArgs(arrayOf("-?"))

        val hasHelpOption = sut.has_help_option()

        assertThat(hasHelpOption).isTrue()
    }

    @Test
    fun has_help_option_for_long_help_option() {
        val sut = ProgramArgs(arrayOf("--help"))

        val hasHelpOption = sut.has_help_option()

        assertThat(hasHelpOption).isTrue()
    }

    @Test
    fun has_no_help_option_for_wrong_number_of_argumenbts() {
        val sut = ProgramArgs(arrayOf("-?", "x"))

        val hasHelpOption = sut.has_help_option()

        assertThat(hasHelpOption).isFalse()
    }

    @Test
    fun has_version_option_for_version_option() {
        val sut = ProgramArgs(arrayOf("-v"))

        val hastVersionOption = sut.has_version_option()

        assertThat(hastVersionOption).isTrue()
    }

    @Test
    fun has_version_option_for_long_version_option() {
        val sut = ProgramArgs(arrayOf("--version"))

        val hastVersionOption = sut.has_version_option()

        assertThat(hastVersionOption).isTrue()
    }

    @Test
    fun has_no_version_option_for_wrong_number_of_arguments() {
        val sut = ProgramArgs(arrayOf("-v", "x"))

        val hastVersionOption = sut.has_version_option()

        assertThat(hastVersionOption).isFalse()
    }

    @Test
    fun has_no_version_option_for_wrong_number_of_arguments_with_long_version_option() {
        val sut = ProgramArgs(arrayOf("--version", "x"))

        val hastVersionOption = sut.has_version_option()

        assertThat(hastVersionOption).isFalse()
    }

    @Test
    fun has_no_version_option_for_mssing_correct_option() {
        val sut = ProgramArgs(arrayOf("x"))

        val hastVersionOption = sut.has_version_option()

        assertThat(hastVersionOption).isFalse()
    }

    @Test
    fun has_no_help_option_for_wrong_number_of_argumenbts_with_long_help_option() {
        val sut = ProgramArgs(arrayOf("--help", "x"))

        val hasHelpOption = sut.has_help_option()

        assertThat(hasHelpOption).isFalse()
    }

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

    @Test
    fun gets_pairing_partner_for_correct_pairing_partner_arguments() {
        val sut = ProgramArgs(arrayOf("-p", "claus-berni-dominik-andrej-shubi-tibor-nandor"))

        val pairingPartner = sut.get_pairing_partner()

        assertThat(pairingPartner).isEqualTo(listOf("claus", "berni", "dominik", "andrej", "shubi", "tibor", "nandor"))
    }

    @Test
    fun throws_when_pairing_partner_arguments_are_missing_the_pairing_partner_option() {
        val sut = ProgramArgs(arrayOf("-x", pairingPartner))

        assertThatThrownBy { sut.get_pairing_partner() }
    }

    @Test
    fun throws_when_pairing_partner_arguments_are_missing_the_pairing_partner() {
        val sut = ProgramArgs(arrayOf("-p"))

        assertThatThrownBy { sut.get_pairing_partner() }
    }

    @Test
    fun throws_when_pairing_partner_arguments_are_wrong_order() {
        val sut = ProgramArgs(arrayOf(pairingPartner, "-p"))

        assertThatThrownBy { sut.get_pairing_partner() }
    }

    @Test
    fun throws_when_pairing_partner_have_the_wrong_format() {
        val sut = ProgramArgs(arrayOf("-p", "clausbern"))

        assertThatThrownBy { sut.get_pairing_partner() }
    }

    @Test
    fun throws_when_number_of_pairing_partneris_too_little() {
        val sut = ProgramArgs(arrayOf("-p", "claus-berni-dominik-andrej-shubi-tibor"))

        assertThatThrownBy { sut.get_pairing_partner() }
    }

    @Test
    fun throws_when_number_of_pairing_partneris_too_high() {
        val sut = ProgramArgs(arrayOf("-p", "$pairingPartner-mihai"))

        assertThatThrownBy { sut.get_pairing_partner() }
    }

}
