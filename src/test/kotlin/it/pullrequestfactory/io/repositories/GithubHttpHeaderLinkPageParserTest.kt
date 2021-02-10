package it.pullrequestfactory.io.repositories

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.io.repositories.GithubHttpHeaderLinkPageParser

class GithubHttpHeaderLinkPageParserTest {

    @Test
    fun `default value for null`() {
        val pages = GithubHttpHeaderLinkPageParser.parsePages(linkHeader = null)

        assertThat(pages).isEqualTo(1..1)
    }

    @Test
    fun `pages for a single page`() {
        val pages = GithubHttpHeaderLinkPageParser.parsePages(linkHeader = "page=1")

        assertThat(pages).isEqualTo(1..1)
    }

    @Test
    fun `pages for 100 pages`() {
        val pages = GithubHttpHeaderLinkPageParser.parsePages(linkHeader = "page=100")

        assertThat(pages).isEqualTo(1..100)
    }

    @Test
    fun `default value for missing page in link header`() {
        val pages = GithubHttpHeaderLinkPageParser.parsePages(linkHeader = "foo")

        assertThat(pages).isEqualTo(1..1)
    }

}
