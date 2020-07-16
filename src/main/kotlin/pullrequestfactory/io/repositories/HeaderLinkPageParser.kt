package pullrequestfactory.io.repositories

class HeaderLinkPageParser {

    private val DEFAULT_VALUE = 1..1
    private val IDX_OF_MATCHED_VAL = 1
    private val PATTERN = "page=([0-9]+)".toRegex()

    fun parse_pages(linkHeader: String?): IntRange {
        return when (linkHeader) {
            null -> DEFAULT_VALUE
            else -> last_page_number_in(linkHeader)
        }
    }

    private fun last_page_number_in(linkHeader: String): IntRange {
        val matches = PATTERN.findAll(linkHeader)
        return when {
            matches.none() -> DEFAULT_VALUE
            else -> 1..matches.last().groupValues[IDX_OF_MATCHED_VAL].toInt()
        }
    }

}
