package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.responses.Response
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.repositories.GithubHttpHeaderLinkPageParser.parse_pages

class GithubHttpReadRepo(val httpClient: HttpClient, val ui: UI) {

    inline fun <reified T> get_list(res: Response, url: String): List<T> {
        val pages = parse_pages(res.headers["link"])
        val branches = mutableListOf<List<T>>()
        pages.forEach {
            val pagedUrl = "$url?page=$it"
            val response = httpClient.get(pagedUrl)
            handle(response, pagedUrl)
            val json = response.text
            branches.add((Klaxon().parseArray(json) ?: emptyList()))
        }
        return branches.flatten()
    }

    fun handle(response: Response, url: String) {
        when (response.statusCode) {
            403 -> ui.show("Too many requests to Github within time limit")
            404 -> ui.show("Couldn't find following URL: $url")
            else -> ui.show(response.toString())
        }
    }
}
