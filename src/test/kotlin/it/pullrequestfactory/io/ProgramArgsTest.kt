package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
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
        val pairingPartner = "pp1-pp2-pp3-pp4-pp5-pp6-pp7"
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

}

class TestUI() : UI {
    var shownMessage: String = ""

    override fun show(msg: String) {
        shownMessage = msg
    }

}
