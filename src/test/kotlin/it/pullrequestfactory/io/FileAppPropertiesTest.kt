package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import pullrequestfactory.io.programs.impl.FileAppProperties
import java.nio.file.Files
import java.nio.file.Paths

class FileAppPropertiesTest {

    private val propsFileName = "tmp.properties"

    @After
    fun tearDown() {
        Files.deleteIfExists(Paths.get("target/test-classes/$propsFileName"))
    }

    @Test
    fun get_base_url_from_given_file() {
        val fileName = createPropsWith("baseUrl=http://localhost:8080")
        val sut = FileAppProperties(fileName)

        val baseUrl = sut.get_github_base_url()

        assertThat(baseUrl).isEqualTo("http://localhost:8080")
    }

    @Test
    fun get_project_version_from_given_file() {
        val fileName = createPropsWith("projectVersion=1.0-SNAPSHOT")
        val sut = FileAppProperties(fileName)

        val baseUrl = sut.get_project_version()

        assertThat(baseUrl).isEqualTo("1.0-SNAPSHOT")
    }

    @Test
    fun get_default_base_url_in_case_file_does_not_exist() {
        val fileName = createPropsWith("x=y")
        val sut = FileAppProperties(fileName)

        val baseUrl = sut.get_github_base_url()

        assertThat(baseUrl).isEqualTo("http://localhost")
    }

    private fun createPropsWith(prop: String): String {
        Files.write(Paths.get("target/test-classes/$propsFileName"), prop.toByteArray())
        return propsFileName
    }

}
