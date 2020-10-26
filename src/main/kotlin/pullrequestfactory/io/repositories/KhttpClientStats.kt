package pullrequestfactory.io.repositories

import khttp.responses.Response

class KhttpClientStats {

    private var getCounter: Int = 0
    private var postCounter: Int = 0

    fun get(url: String): Response {
        getCounter = getCounter.inc()
        return khttp.get(url)
    }

    fun getCounter(): Int {
        return getCounter
    }

    fun postCounter(): Int {
        return postCounter
    }

    fun resetGetCounter() {
        getCounter = 0
    }

    fun resetPostCounter() {
        postCounter = 0
    }

    fun post(url: String, headers: Map<String, String>, data: String): Response {
        postCounter = postCounter.inc()
        return khttp.post(url, headers, data = data)
    }

}
