package it.pullrequestfactory.io

import khttp.requests.Request
import khttp.responses.Response
import khttp.structures.cookie.CookieJar
import org.assertj.core.api.Assertions.assertThat
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Test
import pullrequestfactory.io.KHttpResponseLastPageParser
import java.io.InputStream
import java.net.HttpURLConnection
import java.nio.charset.Charset

class KHttpResponseLastPageParserTest {

    @Test
    fun last_page_of_branches_will_be_negative_when_link_header_does_not_exist() {
        val sut = KHttpResponseLastPageParser()

        val lastPage = sut.last_page_of_branches(responseWithHeaders(emptyMap()))

        assertThat(lastPage).isEqualTo("-1")
    }

    @Test
    fun last_page_of_branches_will_be_parsed_from_given_headers() {
        val sut = KHttpResponseLastPageParser()

        val lastPage = sut.last_page_of_branches(responseWithHeaders(mapOf("link" to "page=1")))

        assertThat(lastPage).isEqualTo("1")
    }

    @Test
    fun last_page_of_branches_will_be_negative_if_link_header_is_not_in_expected_format() {
        val sut = KHttpResponseLastPageParser()

        val lastPage = sut.last_page_of_branches(responseWithHeaders(mapOf("link" to "foo")))

        assertThat(lastPage).isEqualTo("-1")
    }

    private fun responseWithHeaders(hdrs: Map<String, String>): Response {
        return object : Response {
            override val connection: HttpURLConnection
                get() = IGNORE("not needed for tests")
            override val content: ByteArray
                get() = IGNORE("not needed for tests")
            override val cookies: CookieJar
                get() = IGNORE("not needed for tests")
            override var encoding: Charset
                get() = IGNORE("not needed for tests")
                set(value) {
                    IGNORE("not implemented $value")
                }
            override val headers: Map<String, String>
                get() = hdrs
            override val history: List<Response>
                get() = IGNORE("not needed for tests")
            override val jsonArray: JSONArray
                get() = IGNORE("not needed for tests")
            override val jsonObject: JSONObject
                get() = IGNORE("not needed for tests")
            override val raw: InputStream
                get() = IGNORE("not needed for tests")
            override val request: Request
                get() = IGNORE("not needed for tests")
            override val statusCode: Int
                get() = IGNORE("not needed for tests")
            override val text: String
                get() = IGNORE("not needed for tests")
            override val url: String
                get() = IGNORE("not needed for tests")

            override fun contentIterator(chunkSize: Int): Iterator<ByteArray> {
                IGNORE("not needed for tests")
            }

            override fun lineIterator(chunkSize: Int, delimiter: ByteArray?): Iterator<ByteArray> {
                IGNORE("not needed for tests")
            }
        }
    }

    private fun IGNORE(reason: String): Nothing = throw NotImplementedError("An operation is not implemented: $reason")

}
