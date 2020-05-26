package pullrequestfactory.io

import pullrequestfactory.domain.Candidate

class ProgramArgs(private val args: Array<String>) {

    private val backendChapterTeamMembers = listOf("claus", "berni", "nandor", "dominik", "mihai", "lampe", "shubi",
            "markus", "tibor", "christian", "michal", "tomas", "peter", "martin", "john", "andrej")

    val candidate: Candidate
        get() {
            val candidateFullName = args[0]
            return Candidate(candidateFullName.split("-")[0], candidateFullName.split("-")[1])
        }

    val basicAuthToken: String = args[1]

    val pairingPartner: List<String>
        get() {
            return args[2].split("-")
        }

    fun areValid(): Boolean {
        return errorMessage().isEmpty()
    }

    private fun errorMessage(): String {
        return when {
            args.size != 3 -> "java -jar <EXEC_JAR>.jar <CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME> <BASIC_AUTH_TOKEN> <[PAIRING_PARTNER]>"
            !args[0].contains("-") -> "Candidate first name and last name must be separated by hypen: <CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME>"
            args[2].split("-").size != 7 -> "Exactly 7 pairing partner must be provided separated by hyphen"
            args[2].split("-").filter {
                backendChapterTeamMembers.contains(it.toLowerCase())
            }.size != 7 -> "At least one provided pairing partner is unknown. Please check the list of provided pairing partner."
            else -> ""
        }
    }

    fun printErrorMessage() {
        println(errorMessage())
    }

}
