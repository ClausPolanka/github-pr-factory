package pullrequestfactory

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.io.GithubFileReadRepo
import pullrequestfactory.io.GithubHttpRepo

fun main(args: Array<String>) {
    when {
        args.size != 2 -> println("java -jar <EXEC_JAR>.jar <CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME> <BASIC_AUTH_TOKEN>")
        !args[0].contains("-") -> println("Candidate first name and last name must be separated by hypen: <CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME>")
        else -> executeProgram(args)
    }
}

fun executeProgram(args: Array<String>) {
    val candidateFullName = args[0]
    val c = Candidate(candidateFullName.split("-")[0], candidateFullName.split("-")[1])
    val f = GithubPRFactory(
            GithubFileReadRepo(),
            GithubHttpRepo("wordcount", basicAuth = args[1])
    )
    f.create_pull_requests(c)
}
