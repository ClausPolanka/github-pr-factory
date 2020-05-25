package pullrequestfactory

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.io.GithubHttpRepo

val backendChapterTeamMembers = listOf("claus", "berni", "nandor", "dominik", "mihai", "lampe", "shubi", "markus", "tibor", "christian", "michal", "tomas", "peter", "martin", "john")

fun main(args: Array<String>) {
    when {
        args.size != 3 -> println("java -jar <EXEC_JAR>.jar <CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME> <BASIC_AUTH_TOKEN> <[PAIRING_PARTNER]>")
        !args[0].contains("-") -> println("Candidate first name and last name must be separated by hypen: <CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME>")
        args[2].split("-").size != 7 -> println("Exactly 7 pairing partner must be provided separated by hyphen")
        args[2].split("-").filter {
            backendChapterTeamMembers.contains(it)
        }.size != 7 -> println("At least one provided pairing partner is unknown. Please check the list of provided pairing partner.")
        else -> executeProgram(args)
    }
}

fun executeProgram(args: Array<String>) {
    val candidateFullName = args[0]
    val c = Candidate(candidateFullName.split("-")[0], candidateFullName.split("-")[1])
    val pairingPartner = args[2].split("-")
    val githubRepo = GithubHttpRepo("wordcount", basicAuthToken = args[1])
    val f = GithubPRFactory(githubRepo, githubRepo)
    f.create_pull_requests(c, pairingPartner)
}
