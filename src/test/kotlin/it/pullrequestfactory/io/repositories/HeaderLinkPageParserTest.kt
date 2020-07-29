package it.pullrequestfactory.io.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.repositories.HeaderLinkPageParser

class HeaderLinkPageParserTest {

    @Test
    fun default_value_for_null() {
        val pages = HeaderLinkPageParser.parse_pages(linkHeader = null)

        assertThat(pages).isEqualTo(1..1)
    }

    @Test
    fun pages_for_a_single_page() {
        val pages = HeaderLinkPageParser.parse_pages(linkHeader = "page=1")

        assertThat(pages).isEqualTo(1..1)
    }

    @Test
    fun pages_for_100_pages() {
        val pages = HeaderLinkPageParser.parse_pages(linkHeader = "page=100")

        assertThat(pages).isEqualTo(1..100)
    }

    @Test
    fun default_value_for_missing_page_in_link_header() {
        val pages = HeaderLinkPageParser.parse_pages(linkHeader = "foo")

        assertThat(pages).isEqualTo(1..1)
    }

}
