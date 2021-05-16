package pullrequestfactory.io.programs.impl

import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

@Serializable
data class RateLimit(val rate: Rate) {

    fun localResetDateTime() = toLocalDateTime(rate.reset)

    private fun toLocalDateTime(resetDateTime: Instant): String {
        val local = LocalDateTime.ofInstant(resetDateTime, ZoneOffset.UTC)
        return local.format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
    }

    fun isExeeded(requiredNrOfRequests: Int) = rate.remaining < requiredNrOfRequests

}
