package pullrequestfactory.io.programs.impl

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

data class RateLimit(val rate: Rate) {

    fun localResetDateTime(): String {
        return to_local_date_time(rate.reset)
    }

    private fun to_local_date_time(resetDateTime: Instant): String {
        val local = LocalDateTime.ofInstant(resetDateTime, ZoneOffset.UTC)
        return local.format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
    }

    fun isExeeded(requiredNrOfRequests: Int): Boolean {
        return rate.remaining < requiredNrOfRequests
    }

}
