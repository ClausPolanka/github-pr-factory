package it.pullrequestfactory

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import pullrequestfactory.main
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MainKtTest {

    private val systemIn = System.`in`
    private val systemOut = System.out
    private val userOutput = ByteArrayOutputStream()

    @Before
    fun setUp() {
        System.setOut(PrintStream(userOutput))
    }

    @After
    fun tearDown() {
        System.setIn(systemIn)
        System.setOut(systemOut)
    }

    @Test
    fun no_pull_requets_created_for_wrong_number_of_arguments() {
        val args = emptyArray<String>()
        main(args)
        assertThat(userOutput.toString()).contains("Wrong number of arguments")
    }

}
