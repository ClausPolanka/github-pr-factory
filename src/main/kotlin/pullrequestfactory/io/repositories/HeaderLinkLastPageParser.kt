package pullrequestfactory.io.repositories

class HeaderLinkLastPageParser {

    private val DEFAULT_VALUE = "-1"
    private val IDX_OF_MATCHED_VAL = 1
    private val pattern = "page=([0-9]+)".toRegex()

    fun last_page_of_branches(linkHeader: String?): String {
        return when (linkHeader) {
            null -> DEFAULT_VALUE
            else -> {
                val matches = pattern.findAll(linkHeader)
                when {
                    matches.none() -> DEFAULT_VALUE
                    else -> matches.last().groupValues[IDX_OF_MATCHED_VAL]
                }
            }
        }
    }

}
