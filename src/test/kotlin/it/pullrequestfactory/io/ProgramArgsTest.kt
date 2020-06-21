package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.UI
import pullrequestfactory.io.ProgramArgs

class ProgramArgsTest {

    @Test
    fun prints_error_message_for_empty_arguments() {
        val ui = TestUI()
        val sut = ProgramArgs(emptyArray(), ui)

        sut.areValid()
        sut.printErrorMessage()

        assertThat(sut.programUsageMsg).isEqualTo(ui.shownMessage)
    }

    @Test
    fun prints_error_message_for_arguments_containing_only_one_element() {
        val ui = TestUI()
        val sut = ProgramArgs(arrayOf("arg1"), ui)

        sut.areValid()
        sut.printErrorMessage()

        assertThat(sut.programUsageMsg).isEqualTo(ui.shownMessage)
    }

    @Test
    fun prints_error_message_for_arguments_containing_only_two_elements() {
        val ui = TestUI()
        val sut = ProgramArgs(arrayOf("arg1", "arg2"), ui)

        sut.areValid()
        sut.printErrorMessage()

        assertThat(sut.programUsageMsg).isEqualTo(ui.shownMessage)
    }

    @Test
    fun prints_error_message_for_candidate_argument_not_having_the_correct_syntax() {
        val ui = TestUI()
        val candidate = "arg1"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", "arg3"), ui)

        sut.areValid()
        sut.printErrorMessage()

        assertThat(sut.candidateSyntaxMsg).isEqualTo(ui.shownMessage)

    }

    @Test
    fun prints_error_message_for_pairing_partner_argument_having_the_wrong_count() {
        val ui = TestUI()
        val candidate = "firstname-lastname"
        val pairingPartner = "arg3"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", pairingPartner), ui)

        sut.areValid()
        sut.printErrorMessage()

        assertThat(sut.nrOfRequiredPairingPartnerMsg).isEqualTo(ui.shownMessage)
    }

    @Test
    fun prints_error_message_for_pairing_partner_argument_containing_at_least_one_unknown_member() {
        val ui = TestUI()
        val candidate = "firstname-lastname"
        val pairingPartner = "unknown-berni-mihai-nandor-lampe-christian-shubi"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", pairingPartner), ui)

        sut.areValid()
        sut.printErrorMessage()

        assertThat(sut.unknownPairingPartnerMsg).isEqualTo(ui.shownMessage)
    }

    @Test
    fun program_arguments_are_valid_if_all_arguments_are_provided_and_have_the_expected_syntax() {
        val candidate = "firstname-lastname"
        val pairingPartner = "claus-berni-mihai-nandor-lampe-christian-shubi"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", pairingPartner), TestUI())

        val areValid = sut.areValid()

        assertThat(areValid).isTrue()
    }

    @Test
    fun candidate_is_correct_when_args_contains_candidate_and_name_has_correct_syntax() {
        val sut = ProgramArgs(arrayOf("firstname-lastname"), TestUI())

        val c = sut.candidate

        assertThat(c).isEqualTo(Candidate("firstname", "lastname"))
    }

    @Test
    fun candidate_gets_default_name_when_args_are_empty() {
        val sut = ProgramArgs(emptyArray(), TestUI())

        val c = sut.candidate

        assertThat(c).isEqualTo(Candidate("Wrong", "Candidate"))
    }

    @Test
    fun candidate_gets_default_name_when_args_contain_name_without_hyphen_separator() {
        val sut = ProgramArgs(arrayOf("firstnamelastname"), TestUI())

        val c = sut.candidate

        assertThat(c).isEqualTo(Candidate("Wrong", "Candidate"))
    }

    @Test
    fun basic_auth_token_is_set_when_args_contain_token_as_second_value() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "any-basic-auth-token"), TestUI())

        val basicAuthToken = sut.basicAuthToken

        assertThat(basicAuthToken).isEqualTo("any-basic-auth-token")
    }

    @Test
    fun basic_auth_token_is_empty_when_args_does_not_contain_token_as_second_value() {
        val sut = ProgramArgs(arrayOf("firstname-lastname"), TestUI())

        val basicAuthToken = sut.basicAuthToken

        assertThat(basicAuthToken).isEmpty()
    }

    @Test
    fun pairing_partner_is_set_when_args_contain_names_separated_by_hyphen_as_third_value() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "any-basic-auth-token", "claus-berni"), TestUI())

        val pairingPartner = sut.pairingPartner

        assertThat(pairingPartner).containsExactly("claus", "berni")
    }

    @Test
    fun pairing_partner_is_set_when_args_contain_one_name_as_third_value() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "any-basic-auth-token", "claus"), TestUI())

        val pairingPartner = sut.pairingPartner

        assertThat(pairingPartner).containsExactly("claus")
    }

    @Test
    fun pairing_partner_is_set_when_args_contain_token_as_second_value() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "any-basic-auth-token"), TestUI())

        val pairingPartner = sut.pairingPartner

        assertThat(pairingPartner).isEmpty()
    }

    @Test
    fun pairing_partner_is_set_when_args_contain_token_as_second_value2() {
        val sut = ProgramArgs(arrayOf("firstname-lastname", "any-basic-auth-token"), TestUI())

        val pairingPartner = sut.pairingPartner

        assertThat(pairingPartner).isEmpty()
    }
}

class TestUI : UI {
    var shownMessage: String = ""

    override fun show(msg: String) {
        shownMessage = msg
    }

}
