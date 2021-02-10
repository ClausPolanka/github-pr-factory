package it.pullrequestfactory.io.uis

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import pullrequestfactory.io.uis.ConsoleUI
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class ConsoleUITest {

    private val systemIn = System.`in`
    private val systemOut = System.out
    private val programOutput = ByteArrayOutputStream()

    @Before
    fun setUp() {
        System.setOut(PrintStream(programOutput))
    }

    @After
    fun tearDown() {
        System.setIn(systemIn)
        System.setOut(systemOut)
    }

    @Test
    fun `returns user input entered by the user`() {
        userEnters("user input")
        val sut = ConsoleUI()

        val actual = sut.getUserInput(msg = "any")

        assertThat(actual).isEqualTo("user input")
    }

    private fun userEnters(userInput: String) {
        val input = ByteArrayInputStream(userInput.toByteArray())
        System.setIn(input)
    }
}
