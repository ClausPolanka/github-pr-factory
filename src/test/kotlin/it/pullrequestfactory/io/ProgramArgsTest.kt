package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.io.programs.ProgramArgs

class ProgramArgsTest {

    private val GITHUB_BASIC_AUTH_TOKEN = "asdfkk3282kas8ölash8"
    private val PAIRING_PARTNER = "claus-berni-dominik-andrej-shubi-tibor-nandor"

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
    fun has_no_help_option_for_wrong_number_of_argumenbts_with_long_help_option() {
        val sut = ProgramArgs(arrayOf("--help", "x"))

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
    fun has_open_command_help_option_for_correct_arguments() {
        val sut = ProgramArgs(arrayOf("open", "--help"))

        val hasOpenCommandHelpOption = sut.has_open_command_help_option()

        assertThat(hasOpenCommandHelpOption).isTrue()
    }

    @Test
    fun has_no_open_command_help_option_for_missing_long_help_option() {
        val sut = ProgramArgs(arrayOf("open", "-?"))

        val hasOpenCommandHelpOption = sut.has_open_command_help_option()

        assertThat(hasOpenCommandHelpOption).isFalse()
    }

    @Test
    fun has_no_open_command_help_option_for_missing_command() {
        val sut = ProgramArgs(arrayOf("close", "--help"))

        val hasOpenCommandHelpOption = sut.has_open_command_help_option()

        assertThat(hasOpenCommandHelpOption).isFalse()
    }

    @Test
    fun has_close_command_help_option_for_correct_arguments() {
        val sut = ProgramArgs(arrayOf("close", "--help"))

        val hasCloseCommandHelpOption = sut.has_close_command_help_option()

        assertThat(hasCloseCommandHelpOption).isTrue()
    }

    @Test
    fun has_no_close_command_help_option_for_missing_long_help_option() {
        val sut = ProgramArgs(arrayOf("close", "-?"))

        val hasCloseCommandHelpOption = sut.has_close_command_help_option()

        assertThat(hasCloseCommandHelpOption).isFalse()
    }

    @Test
    fun has_no_close_command_help_option_for_missing_command() {
        val sut = ProgramArgs(arrayOf("open", "--help"))

        val hasCloseCommandHelpOption = sut.has_close_command_help_option()

        assertThat(hasCloseCommandHelpOption).isFalse()
    }

    @Test
    fun has_valid_open_command_for_correct_arguments() {
        val sut = ProgramArgs(arrayOf(
                "open",
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN,
                "-p",
                PAIRING_PARTNER))

        val hasInvalidOpenCommand = sut.has_invalid_open_command()

        assertThat(hasInvalidOpenCommand).isFalse()
    }

    @Test
    fun has_valid_open_command_with_optional_options_for_correct_arguments() {
        val sut = ProgramArgs(arrayOf(
                "open",
                "-l",
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN,
                "-p",
                PAIRING_PARTNER))

        val hasInvalidOpenCommand = sut.has_invalid_open_command()

        assertThat(hasInvalidOpenCommand).isFalse()
    }

    @Test
    fun has_valid_open_command_with_optional_options_long_version_for_correct_arguments() {
        val sut = ProgramArgs(arrayOf(
                "open",
                "--last-finished",
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN,
                "-p",
                PAIRING_PARTNER))

        val hasInvalidOpenCommand = sut.has_invalid_open_command()

        assertThat(hasInvalidOpenCommand).isFalse()
    }

    @Test
    fun has_invalid_open_command_because_of_invalid_candidate_syntax() {
        val sut = ProgramArgs(arrayOf(
                "open",
                "-c",
                "firstnamelastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN,
                "-p",
                PAIRING_PARTNER))

        val hasInvalidOpenCommand = sut.has_invalid_open_command()

        assertThat(hasInvalidOpenCommand).isTrue()
    }

    @Test
    fun has_invalid_open_command_because_of_invalid_github_basic_auth_token_syntax() {
        val sut = ProgramArgs(arrayOf(
                "open",
                "-c",
                "firstname-lastname",
                "-x",
                GITHUB_BASIC_AUTH_TOKEN,
                "-p",
                PAIRING_PARTNER))

        val hasInvalidOpenCommand = sut.has_invalid_open_command()

        assertThat(hasInvalidOpenCommand).isTrue()
    }

    @Test
    fun has_invalid_open_command_because_of_invalid_pairing_partner_syntax() {
        val sut = ProgramArgs(arrayOf(
                "open",
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN,
                "-x",
                PAIRING_PARTNER))

        val hasInvalidOpenCommand = sut.has_invalid_open_command()

        assertThat(hasInvalidOpenCommand).isTrue()
    }

    @Test
    fun has_invalid_open_command_because_of_wrong_number_of_arguments_for_optinal_options() {
        val sut = ProgramArgs(arrayOf(
                "open",
                "-last-fin", // wrong
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN,
                "-p",
                PAIRING_PARTNER))

        val hasInvalidOpenCommand = sut.has_invalid_open_command()

        assertThat(hasInvalidOpenCommand).isTrue()
    }

    @Test
    fun has_valid_close_command_for_given_arguments() {
        val sut = ProgramArgs(arrayOf(
                "close",
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN))

        val hasInvalidCloseCommand = sut.has_invalid_close_command()

        assertThat(hasInvalidCloseCommand).isFalse()
    }

    @Test
    fun has_valid_close_command_for_given_interactive_mode_arguments() {
        val args = arrayOf("close", "-i")
        val sut = ProgramArgs(args)

        val hasInvalidCloseCommand = sut.has_invalid_close_command()

        assertThat(hasInvalidCloseCommand)
                .describedAs("correct number of close command arguments: '${args.toList()}'")
                .isFalse()
    }

    @Test
    fun has_valid_close_command_for_given_interactive_mode_arguments_long_version() {
        val args = arrayOf("close", "--interactive")
        val sut = ProgramArgs(args)

        val hasInvalidCloseCommand = sut.has_invalid_close_command()

        assertThat(hasInvalidCloseCommand)
                .describedAs("correct number of close command arguments: '${args.toList()}'")
                .isFalse()
    }

    @Test
    fun has_invalid_close_command_because_of_too_many_arguments() {
        val sut = ProgramArgs(arrayOf(
                "close",
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN,
                "-x"))

        val hasInvalidCloseCommand = sut.has_invalid_close_command()

        assertThat(hasInvalidCloseCommand).isTrue()
    }

    @Test
    fun has_invalid_close_command_because_of_invalid_canddiate_syntax() {
        val sut = ProgramArgs(arrayOf(
                "close",
                "-c",
                "firstnamelastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN))

        val hasInvalidCloseCommand = sut.has_invalid_close_command()

        assertThat(hasInvalidCloseCommand).isTrue()
    }

    @Test
    fun has_invalid_close_command_because_of_invalid_github_basic_auth_token_syntax() {
        val sut = ProgramArgs(arrayOf(
                "close",
                "-c",
                "firstname-lastname",
                "-x",
                GITHUB_BASIC_AUTH_TOKEN))

        val hasInvalidCloseCommand = sut.has_invalid_close_command()

        assertThat(hasInvalidCloseCommand).isTrue()
    }

    @Test
    fun has_invalid_close_command_because_of_missing_close_command() {
        val sut = ProgramArgs(arrayOf(
                "-c",
                "firstname-lastname",
                "-g",
                GITHUB_BASIC_AUTH_TOKEN))

        val hasInvalidCloseCommand = sut.has_invalid_close_command()

        assertThat(hasInvalidCloseCommand).isFalse()
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
                .isInstanceOf(ProgramArgs.WrongCandidateArgumentSyntax::class.java)

    }

    @Test
    fun throws_when_candidate_arguments_are_missing_a_candidate() {
        val sut = ProgramArgs(arrayOf("-c"))

        assertThatThrownBy { sut.get_candidate() }
                .isInstanceOf(ProgramArgs.WrongCandidateArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_candidate_arguments_are_missing_a_candidate_in_correct_format() {
        val sut = ProgramArgs(arrayOf("-c", "firstname"))

        assertThatThrownBy { sut.get_candidate() }
                .isInstanceOf(ProgramArgs.WrongCandidateArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_candidate_arguments_are_in_wrong_order() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "-c"))

        assertThatThrownBy { sut.get_candidate() }
                .isInstanceOf(ProgramArgs.WrongCandidateArgumentSyntax::class.java)
    }

    @Test
    fun gets_github_basic_auth_token_for_correct_github_basic_auth_arguments() {
        val sut = ProgramArgs(arrayOf("-g", GITHUB_BASIC_AUTH_TOKEN))

        val token = sut.get_github_basic_auth_token()

        assertThat(token).isEqualTo(GITHUB_BASIC_AUTH_TOKEN)
    }

    @Test
    fun throws_when_github_basic_auth_token_arguments_are_missing_the_github_basic_auth_token_option() {
        val sut = ProgramArgs(arrayOf("-x", GITHUB_BASIC_AUTH_TOKEN))

        assertThatThrownBy { sut.get_github_basic_auth_token() }
                .isInstanceOf(ProgramArgs.WrongGithubBasicAuthTokenArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_github_basic_auth_token_arguments_are_missing_the_github_basic_auth_token() {
        val sut = ProgramArgs(arrayOf("-g"))

        assertThatThrownBy { sut.get_github_basic_auth_token() }
                .isInstanceOf(ProgramArgs.WrongGithubBasicAuthTokenArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_github_basic_auth_token_arguments_are_in_wrong_order() {
        val sut = ProgramArgs(arrayOf(GITHUB_BASIC_AUTH_TOKEN, "-g"))

        assertThatThrownBy { sut.get_github_basic_auth_token() }
                .isInstanceOf(ProgramArgs.WrongGithubBasicAuthTokenArgumentSyntax::class.java)
    }

    @Test
    fun gets_pairing_partner_for_correct_pairing_partner_arguments() {
        val sut = ProgramArgs(arrayOf("-p", "claus-berni-dominik-andrej-shubi-tibor-nandor"))

        val pairingPartner = sut.get_pairing_partner()

        assertThat(pairingPartner).isEqualTo(listOf(
                PairingPartner.CLAUS,
                PairingPartner.BERNHARD,
                PairingPartner.DOMINIK,
                PairingPartner.ANDREJ,
                PairingPartner.SHUBI,
                PairingPartner.TIBOR,
                PairingPartner.NANDOR))
    }

    @Test
    fun throws_when_one_pairing_partner_is_unknown() {
        val sut = ProgramArgs(arrayOf("-p", "claus-berni-dominik-andrej-shubi-tibor-xxx"))

        assertThatThrownBy { sut.get_pairing_partner() }
                .isInstanceOf(ProgramArgs.WrongPairingPartnerArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_pairing_partner_arguments_are_missing_the_pairing_partner_option() {
        val sut = ProgramArgs(arrayOf("-x", PAIRING_PARTNER))

        assertThatThrownBy { sut.get_pairing_partner() }
                .isInstanceOf(ProgramArgs.WrongPairingPartnerArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_pairing_partner_arguments_are_missing_the_pairing_partner() {
        val sut = ProgramArgs(arrayOf("-p"))

        assertThatThrownBy { sut.get_pairing_partner() }
                .isInstanceOf(ProgramArgs.WrongPairingPartnerArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_pairing_partner_arguments_are_wrong_order() {
        val sut = ProgramArgs(arrayOf(PAIRING_PARTNER, "-p"))

        assertThatThrownBy { sut.get_pairing_partner() }
                .isInstanceOf(ProgramArgs.WrongPairingPartnerArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_pairing_partner_have_the_wrong_format() {
        val sut = ProgramArgs(arrayOf("-p", "clausbern"))

        assertThatThrownBy { sut.get_pairing_partner() }
                .isInstanceOf(ProgramArgs.WrongPairingPartnerArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_number_of_pairing_partneris_too_little() {
        val sut = ProgramArgs(arrayOf("-p", "claus-berni-dominik-andrej-shubi-tibor"))

        assertThatThrownBy { sut.get_pairing_partner() }
                .isInstanceOf(ProgramArgs.WrongPairingPartnerArgumentSyntax::class.java)
    }

    @Test
    fun throws_when_number_of_pairing_partneris_too_high() {
        val sut = ProgramArgs(arrayOf("-p", "$PAIRING_PARTNER-mihai"))

        assertThatThrownBy { sut.get_pairing_partner() }
                .isInstanceOf(ProgramArgs.WrongPairingPartnerArgumentSyntax::class.java)
    }

}
