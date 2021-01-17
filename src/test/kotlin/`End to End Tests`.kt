import com.github.ajalt.clikt.core.CliktCommand
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Ignore
import org.junit.Test
import pullrequestfactory.main

class `End to End Tests` {

    @Test
    @Ignore("Change main to parse before executing")
    fun `shows program's version output to the user`() {
        val `version output` = "github-pr-factory version 1.3.0-SNAPSHOT"
        execute(::`github-pr-factory with`, arrayOf("--version"), `and expect` = `version output`)
    }

    @Test
    @Ignore("Needs WireMock")
    fun `shows program's OPEN command help output to the user`() {
        val `version output` = "github-pr-factory version 1.3.0-SNAPSHOT"
        execute(::`github-pr-factory with`,
                arrayOf("open", "-fn", "firstname", "-ln", "lastname",
                        "-pp1", "markus",
                        "-pp2", "berni",
                        "-pp3", "lukas",
                        "-pp4", "jakub",
                        "-pp5", "peter",
                        "-pp6", "christian",
                        "-pp7", "vaclav",
                ), `and expect` = `version output`)
    }

    @Test
    @Ignore("Help option returns null")
    fun `shows program's help output to the user`() {
        val `help output` = "help output"
        execute(::`github-pr-factory with`, arrayOf("--help"), `and expect` = `help output`)
    }

    private fun `github-pr-factory with`(args: Array<String>) {
        main(args)
    }

    fun execute(fn: (args: Array<String>) -> Unit, args: Array<String>, `and expect`: String) {
        assertThatThrownBy { fn(args) }
                .hasMessage(`and expect`)
    }

    fun CliktCommand.exec(args: Array<String>) {
        println("test")
        parse(args)
    }
}
