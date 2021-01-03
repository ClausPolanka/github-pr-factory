package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.RateLimit

class ProgramRateLimitExeeded(private val rateLimitBefore: RateLimit) : OpenPRProgram {

    override fun execute() {
        println("The limit exeeded for calling the Github API with your Github user")
        println("Please retry at: ${rateLimitBefore.localResetDateTime()}")
    }

}
