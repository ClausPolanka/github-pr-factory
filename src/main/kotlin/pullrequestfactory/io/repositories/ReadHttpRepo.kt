package pullrequestfactory.io.repositories

import com.beust.klaxon.Klaxon
import khttp.get
import khttp.responses.Response

class ReadHttpRepo {

    inline fun <reified T> get_list(res: Response, url: String): List<T> {
        val pages = HeaderLinkPageParser().parse_pages(res.headers["link"])
        val branches = mutableListOf<List<T>>()
        pages.forEach {
            val json = get("$url?page=$it").text
            branches.add((Klaxon().parseArray(json) ?: emptyList()))
        }
        return branches.flatten()
    }

}
