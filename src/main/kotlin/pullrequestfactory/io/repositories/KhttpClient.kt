package pullrequestfactory.io.repositories

import khttp.responses.Response

class KhttpClient : HttpClient {

    override fun get(url: String): Response {
        return khttp.get(url)
    }

    override fun post(url: String, headers: Map<String, String>, data: String): Response {
        return khttp.post(url, headers, data = data)
    }

    override fun patch(url: String, headers: Map<String, String>, data: String): Response {
        return khttp.patch(url, headers, data = data)
    }

}
