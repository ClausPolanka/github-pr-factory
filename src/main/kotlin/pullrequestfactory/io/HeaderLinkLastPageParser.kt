package pullrequestfactory.io

class HeaderLinkLastPageParser {

    private val idxOfMatchedVal = 1

    fun last_page_of_branches(linkHeader: String?): String {
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
