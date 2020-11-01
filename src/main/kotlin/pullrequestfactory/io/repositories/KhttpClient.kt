package pullrequestfactory.io.repositories

import khttp.responses.Response

class KhttpClient(private val basicAuthToken: String) : HttpClient {

    override fun get(url: String): Response {
        return khttp.get(url, headers = mapOf(
                "Accept" to "application/json",
                "Authorization" to "Basic $basicAuthToken",
                "Content-Type" to "application/json"))
    }

    override fun post(url: String, headers: Map<String, String>, data: String): Response {
        return khttp.post(url, headers, data = data)
    }

    override fun patch(url: String, headers: Map<String, String>, data: String): Response {
        return khttp.patch(url, headers, data = data)
    }

}
