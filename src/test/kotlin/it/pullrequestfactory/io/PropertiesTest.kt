package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.ConsoleUI
import pullrequestfactory.io.Properties

class PropertiesTest {

    @Test
    fun get_base_url_from_given_file() {
        val sut = Properties("app.properties", ConsoleUI())

        val baseUrl = sut.getBaseUrl()

        assertThat(baseUrl).isEqualTo("http://localhost:8080")
    }

    @Test
    fun get_default_base_url_in_case_file_does_not_exist() {
        val sut = Properties("invalid.txt", ConsoleUI())

        val baseUrl = sut.getBaseUrl()

        assertThat(baseUrl).isEqualTo("http://localhost")
    }

    @Test
    fun get_default_base_url_in_case_base_url_property_does_not_exist() {
        val sut = Properties("app_missing_base_url.properties.txt", ConsoleUI())

        val baseUrl = sut.getBaseUrl()

        assertThat(baseUrl).isEqualTo("http://localhost")
    }

}
