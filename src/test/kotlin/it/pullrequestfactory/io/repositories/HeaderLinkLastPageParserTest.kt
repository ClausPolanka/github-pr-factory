package it.pullrequestfactory.io.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.repositories.HeaderLinkLastPageParser

class HeaderLinkLastPageParserTest {

    @Test
    fun last_page_of_branches_will_be_negative_when_link_header_does_not_exist() {
        val sut = HeaderLinkLastPageParser()

        val lastPage = sut.last_page_of_branches(linkHeader = null)

        assertThat(lastPage).isEqualTo("-1")
    }

    @Test
    fun last_page_of_branches_will_be_parsed_from_given_link_header() {
        val sut = HeaderLinkLastPageParser()

        val lastPage = sut.last_page_of_branches(linkHeader = "page=1")

        assertThat(lastPage).isEqualTo("1")
    }

    @Test
    fun last_page_of_branches_will_be_negative_if_link_header_is_not_in_expected_format() {
        val sut = HeaderLinkLastPageParser()

        val lastPage = sut.last_page_of_branches(linkHeader = "foo")

        assertThat(lastPage).isEqualTo("-1")
    }

}
