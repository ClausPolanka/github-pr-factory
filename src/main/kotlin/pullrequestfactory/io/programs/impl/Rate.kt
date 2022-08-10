package pullrequestfactory.io.programs.impl

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class Rate(val limit: Int, val remaining: Int, @Contextual val reset: Instant, val used: Int)

