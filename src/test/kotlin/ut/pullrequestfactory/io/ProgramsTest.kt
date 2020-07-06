package ut.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.*

class ProgramsTest {

    @Test
    fun creates_program_which_shows_help() {
        val sut = Programs()

        val program = sut.create_program_for(emptyArray())

        assertThat(program is ShowHelpOutputProgram)
                .describedAs("program shows help output")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_version() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("-v"))

        assertThat(program is ShowVersionOutputProgram)
                .describedAs("program shows version output")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_open_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("open", "--help"))

        assertThat(program is ShowOpenCommandHelpOutputProgram)
                .describedAs("program shows help for open command")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_invalid_open_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("open"))

        assertThat(program is ShowInvalidOpenCommandOutputProgram)
                .describedAs("program shows help for open command")
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

        assertThat(program is CreatePullRequestsProgram)
                .describedAs("program opens pull requests")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_close_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("close", "--help"))

        assertThat(program is ShowCloseCommandHelpOutputProgram)
                .describedAs("program shows help for close command")
                .isTrue()
    }

    @Test
    fun creates_program_which_shows_help_for_invalid_close_command() {
        val sut = Programs()

        val program = sut.create_program_for(arrayOf("close"))

        assertThat(program is ShowInvalidCloseCommandOutputProgram)
                .describedAs("program shows help for close command")
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
                .describedAs("program closes pull requests")
                .isTrue()
    }

}
