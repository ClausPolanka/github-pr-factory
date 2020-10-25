package it.pullrequestfactory.io.programs.impl

import org.assertj.core.api.Assertions
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pullrequestfactory.io.programs.impl.FileUserProperties
import java.nio.file.Files
import java.nio.file.Paths

class FileUserPropertiesTest {

    @Rule @JvmField val tempFolder = TemporaryFolder()

    private val propsFileName = "tmp-user.properties"

    @After
    fun tearDown() {
        Files.deleteIfExists(Paths.get("target/test-classes/$propsFileName"))
    }

    @Test
    fun get_github_basic_auth_token_returns_null_when_file_exists_on_classpath_but_no_property_exists() {
        val fileName = createEmptyPropsFile()
        val sut = FileUserProperties(fileName)

        val basicAuthToken = sut.get_github_basic_auth_token()

        Assertions.assertThat(basicAuthToken).isNull()
    }

    @Test
    fun get_github_basic_auth_token_returns_token_when_file_exists_on_classpath_and_property_exists() {
        val fileName = createPropsWith("githubBasicAuthToken=asdfkj24398")
        val sut = FileUserProperties(fileName)

        val basicAuthToken = sut.get_github_basic_auth_token()

        Assertions.assertThat(basicAuthToken).isEqualTo("asdfkj24398")
    }

    @Test
    fun get_github_basic_auth_token_returns_null_when_file_exists_on_filesystem_but_no_property_exists() {
        val sut = FileUserProperties("tmp-user.properties")

        val basicAuthToken = sut.get_github_basic_auth_token()

        Assertions.assertThat(basicAuthToken).isNull()
    }

    @Test
    fun get_github_basic_auth_token_returns_token_when_file_exists_on_filesystem_and_property_exists() {
        val tempFile = tempFolder.newFile("tmp-user.properties")
        tempFile.writeText("githubBasicAuthToken=asdfkj24398")
        val sut = FileUserProperties(tempFile.absolutePath)

        val basicAuthToken = sut.get_github_basic_auth_token()

        Assertions.assertThat(basicAuthToken).isEqualTo("asdfkj24398")
    }

    private fun createEmptyPropsFile(): String {
        Files.createFile(Paths.get("target/test-classes/$propsFileName"))
        return propsFileName
    }

    private fun createPropsWith(prop: String): String {
        Files.write(Paths.get("target/test-classes/$propsFileName"), prop.toByteArray())
        return propsFileName
    }

}
