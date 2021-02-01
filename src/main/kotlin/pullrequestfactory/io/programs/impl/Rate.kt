package pullrequestfactory.io.programs.impl

import java.time.Instant

data class Rate(val limit: Int, val remaining: Int, val reset: Instant, val used: Int)
