package pullrequestfactory.io.repositories

import khttp.responses.Response

class KhttpClientStats {

    private var getCounter: Int = 0

    fun get(url: String): Response {
        getCounter = getCounter.inc()
        return khttp.get(url)
    }

    fun getCounter(): Int {
        return getCounter
    }

    fun resetGetCounter() {
        getCounter = 0
    }

}
