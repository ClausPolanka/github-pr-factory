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
    fun get_github_auth_token_returns_null_when_file_exists_on_classpath_but_no_property_exists() {
        val fileName = createEmptyPropsFileOnClasspath()
        val sut = FileUserProperties(fileName)

        val authToken = sut.get_github_auth_token()

        assertThat(authToken).isNull()
    }

    @Test
    fun get_github_auth_token_returns_null_when_file_exists_on_classpath_and_property_is_empty() {
        val fileName = createPropsFileOnClasspathWith("githubAuthToken=")
        val sut = FileUserProperties(fileName)

        val authToken = sut.get_github_auth_token()

        assertThat(authToken).isNull()
    }

    @Test
    fun get_github_auth_token_returns_token_when_file_exists_on_classpath_and_property_exists() {
        val fileName = createPropsFileOnClasspathWith("githubAuthToken=asdfkj24398")
        val sut = FileUserProperties(fileName)

        val authToken = sut.get_github_auth_token()

        assertThat(authToken).isEqualTo("asdfkj24398")
    }

    @Test
    fun get_github_auth_token_returns_null_when_file_does_not_exist_on_filesystem() {
        val sut = FileUserProperties(propsFileName)

        val authToken = sut.get_github_auth_token()

        assertThat(authToken).isNull()
    }

    @Test
    fun get_github_auth_token_returns_token_when_file_exists_on_filesystem_and_property_exists() {
        val fileName = createPropsFileOnFileSystemWith("githubAuthToken=asdfkj24398")
        val sut = FileUserProperties(fileName)

        val authToken = sut.get_github_auth_token()

        assertThat(authToken).isEqualTo("asdfkj24398")
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
