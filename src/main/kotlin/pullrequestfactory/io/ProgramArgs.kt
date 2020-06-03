package pullrequestfactory.io

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.UI

class ProgramArgs(private val args: Array<String>, private val ui: UI) {

    val programUsageMsg =
            listOf("[Error]", "Wrong number of arguments", "\n").joinToString(" ") +
                    listOf("[Usage]", "java -jar <EXEC_JAR>.jar",
                            "<CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME>",
                            "<BASIC_AUTH_TOKEN>",
                            "<[PAIRING_PARTNER]>").joinToString(" ")

    val candidateSyntaxMsg = listOf(
            "[Error]",
            "Candidate first name and last name must be separated by hypen:",
            "<CANDIDATE_FIRST_NAME>-<CANDIDATE_LAST_NAME>").joinToString(" ")

    val nrOfRequiredPairingPartnerMsg = listOf(
            "[Error]",
            "Exactly 7 pairing partner must be provided separated by hyphen").joinToString(" ")

    val unknownPairingPartnerMsg = listOf(
            "[Error]",
            "At least one provided pairing partner is unknown. Please check the list of provided pairing partner.")
            .joinToString(" ")

    private val backendChapterTeamMembers = listOf("claus", "berni", "nandor", "dominik", "mihai", "lampe", "shubi",
            "markus", "tibor", "christian", "michal", "tomas", "peter", "martin", "john", "andrej")

    private val indexOfCandidate = 0
    private val indexOfBasicAuthToken = 1
    private val indexOfPairingPartner = 2
    private val nrOfRequiredArgs = 3
    private val nrOfRequiredPairingPartner = 7

    val candidate: Candidate
        get() {
            val candidateFullName = if (args.size >= indexOfCandidate + 1) args[indexOfCandidate] else "Wrong-Candidate"
            val parts = candidateFullName.split("-")
            return if (parts.size == 2) {
                Candidate(parts[indexOfCandidate], parts[1])
            } else Candidate("Wrong", "Candidate")
        }

    val basicAuthToken: String
        get() {
            return if (args.size >= indexOfBasicAuthToken + 1) args[indexOfBasicAuthToken] else ""
        }

    val pairingPartner: List<String>
        get() {
            return if (args.size >= indexOfPairingPartner + 1) pairingPartner() else emptyList()
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

    private fun nrOfPairingPartnerIsWrong() = pairingPartner().size != nrOfRequiredPairingPartner

    private fun atLeastOnePairingPartnerIsUnknown(): Boolean {
        return pairingPartner().filter {
            backendChapterTeamMembers.contains(it.toLowerCase())
        }.size != nrOfRequiredPairingPartner
    }

    private fun pairingPartner() = args[indexOfPairingPartner].split("-")

    fun printErrorMessage() {
        ui.show(errorMessage())
    }

}
