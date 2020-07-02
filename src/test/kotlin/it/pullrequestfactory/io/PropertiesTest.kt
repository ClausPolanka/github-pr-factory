package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import pullrequestfactory.io.Properties
import java.nio.file.Files
import java.nio.file.Paths

class PropertiesTest {

    private val propsFileName = "tmp.properties"

    @After
    fun tearDown() {
        Files.deleteIfExists(Paths.get("target/test-classes/$propsFileName"))
    }

    @Test
    fun get_base_url_from_given_file() {
        val fileName = createPropsWith("baseUrl=http://localhost:8080")
        val sut = Properties(fileName)

        val baseUrl = sut.get_base_url()

        assertThat(baseUrl).isEqualTo("http://localhost:8080")
    }

    @Test
    fun get_default_base_url_in_case_file_does_not_exist() {
        val fileName = createPropsWith("x=y")
        val sut = Properties(fileName)

        val baseUrl = sut.get_base_url()

        assertThat(baseUrl).isEqualTo("http://localhost")
    }

    private fun createPropsWith(prop: String): String {
        Files.write(Paths.get("target/test-classes/$propsFileName"), prop.toByteArray())
        return propsFileName
    }

}
