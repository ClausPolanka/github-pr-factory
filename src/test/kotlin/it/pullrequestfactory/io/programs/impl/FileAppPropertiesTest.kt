package it.pullrequestfactory.io.programs.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Test
import pullrequestfactory.io.programs.impl.FileAppProperties
import java.nio.file.Files
import java.nio.file.Paths

class FileAppPropertiesTest {

    private val propsFileName = "tmp-app.properties"
    private val propsFilePath = "target/test-classes/$propsFileName"

    @After
    fun tearDown() {
        Files.deleteIfExists(Paths.get(propsFilePath))
    }

    @Test
    fun `get base url from given file`() {
        val fileName = createPropsWith("baseUrl=http://localhost:8080")
        val sut = FileAppProperties(fileName)

        val baseUrl = sut.getGithubBaseUrl()

        assertThat(baseUrl).isEqualTo("http://localhost:8080")
    }

    @Test
    fun `get project version from given file`() {
        val fileName = createPropsWith("projectVersion=1.0-SNAPSHOT")
        val sut = FileAppProperties(fileName)

        val baseUrl = sut.getProjectVersion()

        assertThat(baseUrl).isEqualTo("1.0-SNAPSHOT")
    }

    @Test
    fun `get default base url in case file does not exist`() {
        val fileName = createPropsWith("x=y")
        val sut = FileAppProperties(fileName)

        val baseUrl = sut.getGithubBaseUrl()

        assertThat(baseUrl).isEqualTo("http://localhost")
    }

    private fun createPropsWith(prop: String): String {
        Files.write(Paths.get(propsFilePath), prop.toByteArray())
        return propsFileName
    }

}
