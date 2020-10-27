package pullrequestfactory.io.repositories

import khttp.responses.Response

interface HttpClient {
    fun get(url: String): Response
    fun post(url: String, headers: Map<String, String>, data: String): Response
    fun patch(url: String, headers: Map<String, String>, data: String): Response
}
