package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.RateLimit
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class OpenPRProgramRateLimitExeeded(private val rateLimitBefore: RateLimit) : OpenPRProgram {

    override fun execute() {
        println("The limit exeeded for calling the Github API with your Github user")
        println("Please retry at: ${to_local_date_time(rateLimitBefore.rate.reset)}")
    }

    private fun to_local_date_time(resetDateTime: Instant): String {
        val local = LocalDateTime.ofInstant(resetDateTime, ZoneOffset.UTC)
        return local.format(DateTimeFormatter.ofPattern("M/d/y H:m:ss"))
    }

}
