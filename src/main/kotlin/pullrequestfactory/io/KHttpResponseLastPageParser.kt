package pullrequestfactory.io

import khttp.responses.Response

class KHttpResponseLastPageParser {

    private val idxOfMatchedVal = 1
    
    fun last_page_of_branches(response: Response): String {
        val linkHeader = response.headers["link"]
        return if (linkHeader == null) {
            "-1"
        } else {
            val pattern = "page=([0-9]+)".toRegex()
            val matches = pattern.findAll(linkHeader)
            when {
                matches.none() -> "-1"
                else -> matches.last().groupValues[idxOfMatchedVal]
            }
        }
    }

}
