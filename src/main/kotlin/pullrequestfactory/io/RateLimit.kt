package pullrequestfactory.io

import java.time.Instant

data class RateLimit(val rate: Rate)

data class Rate(val limit: Int, val remaining: Int, val reset: Instant, val used: Int)
