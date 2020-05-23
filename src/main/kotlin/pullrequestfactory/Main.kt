package pullrequestfactory

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.io.GithubHttpRepo

fun main(args: Array<String>) {
    val candidateFullName = args[0]
    val c = Candidate(candidateFullName.split("-")[0], candidateFullName.split("-")[1])
    val f = GithubPRFactory(GithubHttpRepo("wordcount", basicAuth = args[1]))
    f.create_pull_requests(c)
}
