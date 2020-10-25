package it.pullrequestfactory.io.programs.impl

import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import pullrequestfactory.io.programs.impl.FileUserProperties
import java.nio.file.Files
import java.nio.file.Paths

class FileUserPropertiesTest {

    @Rule
    @JvmField
    val tempFolder = TemporaryFolder()

    private val propsFileName = "tmp-user.properties"

    @After
    fun tearDown() {
        Files.deleteIfExists(Paths.get("target/test-classes/$propsFileName"))
    }

    @Test
    fun get_github_basic_auth_token_returns_null_when_file_exists_on_classpath_but_no_property_exists() {
        val fileName = createEmptyPropsFileOnClasspath()
        val sut = FileUserProperties(fileName)

        val basicAuthToken = sut.get_github_basic_auth_token()

        assertThat(basicAuthToken).isNull()
    }

    @Test
    fun get_github_basic_auth_token_returns_null_when_file_exists_on_classpath_and_property_is_empty() {
        val fileName = createPropsFileOnClasspathWith("githubBasicAuthToken=")
        val sut = FileUserProperties(fileName)

        val basicAuthToken = sut.get_github_basic_auth_token()

        assertThat(basicAuthToken).isNull()
    }

    @Test
    fun get_github_basic_auth_token_returns_token_when_file_exists_on_classpath_and_property_exists() {
        val fileName = createPropsFileOnClasspathWith("githubBasicAuthToken=asdfkj24398")
        val sut = FileUserProperties(fileName)

        val basicAuthToken = sut.get_github_basic_auth_token()

        assertThat(basicAuthToken).isEqualTo("asdfkj24398")
    }

    @Test
    fun get_github_basic_auth_token_returns_null_when_file_does_not_exist_on_filesystem() {
        val sut = FileUserProperties(propsFileName)

        val basicAuthToken = sut.get_github_basic_auth_token()

        assertThat(basicAuthToken).isNull()
    }

    @Test
    fun get_github_basic_auth_token_returns_token_when_file_exists_on_filesystem_and_property_exists() {
        val fileName = createPropsFileOnFileSystemWith("githubBasicAuthToken=asdfkj24398")
        val sut = FileUserProperties(fileName)

        val basicAuthToken = sut.get_github_basic_auth_token()

        assertThat(basicAuthToken).isEqualTo("asdfkj24398")
    }

    private fun createEmptyPropsFileOnClasspath(): String {
        Files.createFile(Paths.get("target/test-classes/$propsFileName"))
        return propsFileName
    }

    private fun createPropsFileOnClasspathWith(prop: String): String {
        Files.write(Paths.get("target/test-classes/$propsFileName"), prop.toByteArray())
        return propsFileName
    }

    private fun createPropsFileOnFileSystemWith(prop: String): String {
        val propsFile = tempFolder.newFile(propsFileName)
        propsFile.writeText(prop)
        return propsFile.absolutePath
    }

}
