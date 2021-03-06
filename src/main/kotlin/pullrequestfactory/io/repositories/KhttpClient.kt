package pullrequestfactory.io.repositories

import khttp.responses.Response

class KhttpClient(authToken: String) : HttpClient {

    private val defaultHeaders = mapOf(
        "Accept" to "application/json",
        "Authorization" to "token $authToken",
        "Content-Type" to "application/json"
    )

    override fun get(url: String): Response {
        return khttp.get(url, defaultHeaders)
    }

    override fun post(url: String, data: String): Response {
        return khttp.post(url, defaultHeaders, data = data)
    }

    override fun patch(url: String, data: String): Response {
        return khttp.patch(url, defaultHeaders, data = data)
    }

}
