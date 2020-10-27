package pullrequestfactory.io.repositories

import khttp.responses.Response
import java.lang.System.lineSeparator

class KhttpClientStats(private val httpClient: HttpClient) : HttpClient {

    private var getCounter: Int = 0
    private var postCounter: Int = 0
    private var patchCounter: Int = 0

    override fun get(url: String): Response {
        getCounter = getCounter.inc()
        return httpClient.get(url)
    }

    fun getCounter(): Int {
        return getCounter
    }

    fun postCounter(): Int {
        return postCounter
    }

    fun patchCounter(): Int {
        return patchCounter
    }

    fun resetGetCounter() {
        getCounter = 0
    }

    fun resetPostCounter() {
        postCounter = 0
    }

    fun resetPatchCounter() {
        patchCounter = 0
    }

    override fun post(url: String, headers: Map<String, String>, data: String): Response {
        postCounter = postCounter.inc()
        return httpClient.post(url, headers, data = data)
    }

    override fun patch(url: String, headers: Map<String, String>, data: String): Response {
        patchCounter = patchCounter.inc()
        return httpClient.patch(url, headers, data = data)
    }

    fun stats(): String {
        return "get-requests:\t$getCounter ${lineSeparator()}" +
                "post-requests:\t$postCounter ${lineSeparator()}" +
                "patch-requests:\t$patchCounter"
    }
}
