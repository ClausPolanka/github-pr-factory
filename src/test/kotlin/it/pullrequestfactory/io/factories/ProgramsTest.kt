package it.pullrequestfactory.io.factories

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.programs.Program
import pullrequestfactory.io.programs.Programs
import pullrequestfactory.io.programs.impl.*

class ProgramsTest {

    @Test
    fun creates_program_which_shows_help() {
        val sut = Programs()

        val program = sut.create_program_for(emptyArray())

        assertThat(program is ShowHelpOutputProgram)
                .describedAs("program shows help output ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_invalid_arguments() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("foo", "bar", "baz"))

        assertThat(program is ShowHelpOutputProgram)
                .describedAs("program shows help output ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_version() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("-v"))

        assertThat(program is ShowVersionOutputProgram)
                .describedAs("program shows version output ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_open_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("open", "--help"))

        assertThat(program is ShowOpenCommandHelpOutputProgram)
                .describedAs("program shows help for open command ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_invalid_open_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("open"))

        assertThat(program is ShowInvalidOpenCommandOutputProgram)
                .describedAs("program shows help for open command ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_opens_pull_requests() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf(
                "open",
                "-c",
                "firstname-lastname",
                "-g",
                "askdasaeu129",
                "-p",
                "claus-dominik-mihai-christian-berni-markus-shubi"))

        assertThat(program is OpenPullRequestsProgram)
                .describedAs("program opens pull requests ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_opens_pull_requests_and_marks_last_pull_request_as_finished() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf(
                "open",
                "-l",
                "-c",
                "firstname-lastname",
                "-g",
                "askdasaeu129",
                "-p",
                "claus-dominik-mihai-christian-berni-markus-shubi"))

        assertThat(program is OpenPullRequestsProgramWithOptionalOptions)
                .describedAs("program opens pull requests with optinal options ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_close_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("close", "--help"))

        assertThat(program is ShowCloseCommandHelpOutputProgram)
                .describedAs("program shows help for close command ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_invalid_close_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("close"))

        assertThat(program is ShowInvalidCloseCommandOutputProgram)
                .describedAs("program shows help for close command ${actual_program(program)}")
                .isTrue()
    }

    @Test
    fun creates_program_which_closes_pull_requests() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf(
                "close",
                "-c",
                "firstname-lastname",
                "-g",
                "askdasaeu129"))

        assertThat(program is ClosePullRequestsProgram)
                .describedAs("program closes pull requests ${actual_program(program)}")
                .isTrue()
    }

    private fun actual_program(program: Program) = "but was: '${program::class.simpleName}'"

}
