package pullrequestfactory.io

import pullrequestfactory.domain.Candidate

class ProgramArgs(private val args: Array<String>) {

    private val programUsageMsg = listOf(
            "Usage:", "java -jar <EXEC_JAR>.jar",
            "<CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME>",
            "<BASIC_AUTH_TOKEN>",
            "<[PAIRING_PARTNER]>").joinToString(" ")

    private val candidateSyntaxMsg = listOf(
            "Candidate first name and last name must be separated by hypen:",
            "<CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME>").joinToString(" ")

    private val nrOfRequiredPairingPartnerMsg = "Exactly 7 pairing partner must be provided separated by hyphen"

    private val unknownPairingPartnerMsg = "At least one provided pairing partner is unknown. Please check the list of provided pairing partner."

    private val backendChapterTeamMembers = listOf("claus", "berni", "nandor", "dominik", "mihai", "lampe", "shubi",
            "markus", "tibor", "christian", "michal", "tomas", "peter", "martin", "john", "andrej")

    private val indexOfCandidate = 0
    private val indexOfPairingPartner = 2
    private val nrOfRequiredArgs = 3
    private val nrOfRequiredPairingPartner = 7

    val candidate: Candidate
        get() {
            val candidateFullName = args[indexOfCandidate]
            return Candidate(candidateFullName.split("-")[indexOfCandidate], candidateFullName.split("-")[1])
        }

    val basicAuthToken: String = args[1]

    val pairingPartner: List<String>
        get() {
            return args[indexOfPairingPartner].split("-")
        }

    fun areValid(): Boolean {
        return errorMessage().isEmpty()
    }


    private fun errorMessage(): String {
        return when {
            nrOfArgsIsWrong() -> programUsageMsg
            candidateSyntaxIsWrong() -> candidateSyntaxMsg
            nrOfPairingPartnerIsWrong() -> nrOfRequiredPairingPartnerMsg
            atLeastOnePairingPartnerIsUnknown() -> unknownPairingPartnerMsg
            else -> ""
        }
    }

    private fun nrOfArgsIsWrong() = args.size != nrOfRequiredArgs

    private fun candidateSyntaxIsWrong() = !args[indexOfCandidate].contains("-")

    private fun nrOfPairingPartnerIsWrong() = args[indexOfPairingPartner].split("-").size != nrOfRequiredPairingPartner

    private fun atLeastOnePairingPartnerIsUnknown(): Boolean {
        return args[indexOfPairingPartner].split("-").filter {
            backendChapterTeamMembers.contains(it.toLowerCase())
        }.size != nrOfRequiredPairingPartner
    }

    fun printErrorMessage() {
        println(errorMessage())
    }

}
