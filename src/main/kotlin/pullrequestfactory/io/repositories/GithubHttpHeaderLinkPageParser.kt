package pullrequestfactory.io.repositories

object GithubHttpHeaderLinkPageParser {

    private val DEFAULT_VALUE = 1..1
    private const val IDX_OF_MATCHED_VAL = 1
    private val PATTERN = "page=([0-9]+)".toRegex()

    fun parsePages(linkHeader: String?) = when (linkHeader) {
        null -> DEFAULT_VALUE
        else -> lastPageNumberIn(linkHeader)
    }

    private fun lastPageNumberIn(linkHeader: String): IntRange {
        val matches = PATTERN.findAll(linkHeader)
        return when {
            matches.none() -> DEFAULT_VALUE
            else -> 1..matches.last().groupValues[IDX_OF_MATCHED_VAL].toInt()
        }
    }

}
