package it.pullrequestfactory.io

import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.UI
import pullrequestfactory.io.ProgramArgs

class ProgramArgsTest {

    @Test
    fun prints_error_message_for_empty_arguments() {
        val ui = mockk<UI>(relaxed = true)
        val sut = ProgramArgs(emptyArray(), ui)

        sut.areValid()
        sut.printErrorMessage()

        verify { ui.show(sut.programUsageMsg) }
    }

    @Test
    fun prints_error_message_for_arguments_containing_only_one_element() {
        val ui = mockk<UI>(relaxed = true)
        val sut = ProgramArgs(arrayOf("arg1"), ui)

        sut.areValid()
        sut.printErrorMessage()

        verify { ui.show(sut.programUsageMsg) }
    }

    @Test
    fun prints_error_message_for_arguments_containing_only_two_elements() {
        val ui = mockk<UI>(relaxed = true)
        val sut = ProgramArgs(arrayOf("arg1", "arg2"), ui)

        sut.areValid()
        sut.printErrorMessage()

        verify { ui.show(sut.programUsageMsg) }
    }

    @Test
    fun prints_error_message_for_candidate_argument_not_having_the_correct_syntax() {
        val ui = mockk<UI>(relaxed = true)
        val candidate = "arg1"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", "arg3"), ui)

        sut.areValid()
        sut.printErrorMessage()

        verify { ui.show(sut.candidateSyntaxMsg) }
    }

    @Test
    fun prints_error_message_for_pairing_partner_argument_having_the_wrong_count() {
        val ui = mockk<UI>(relaxed = true)
        val candidate = "firstname-lastname"
        val pairingPartner = "arg3"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", pairingPartner), ui)

        sut.areValid()
        sut.printErrorMessage()

        verify { ui.show(sut.nrOfRequiredPairingPartnerMsg) }
    }

    @Test
    fun prints_error_message_for_pairing_partner_argument_containing_at_least_one_unknown_member() {
        val ui = mockk<UI>(relaxed = true)
        val candidate = "firstname-lastname"
        val pairingPartner = "pp1-pp2-pp3-pp4-pp5-pp6-pp7"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", pairingPartner), ui)

        sut.areValid()
        sut.printErrorMessage()

        verify { ui.show(sut.unknownPairingPartnerMsg) }
    }

    @Test
    fun program_arguments_are_valid_if_all_arguments_are_provided_and_have_the_expected_syntax() {
        val ui = mockk<UI>(relaxed = true)
        val candidate = "firstname-lastname"
        val pairingPartner = "claus-berni-mihai-nandor-lampe-christian-shubi"
        val sut = ProgramArgs(arrayOf(candidate, "arg2", pairingPartner), ui)

        val areValid = sut.areValid()

        assertThat(areValid).isTrue()
    }

}
