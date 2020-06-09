package pullrequestfactory.domain

class BranchTitles(private val branches: List<Branch>) {

    fun create(): List<String> {
        val sessions = Sessions(branches).create()
        return branches.mapIndexed { idx, br ->
            val (firstName, lastName, _, iterationNr, pairingPartner) = br.parts()
            "${firstName.capitalize()} ${lastName.capitalize()} " +
                    "Iteration $iterationNr / Session ${sessions[idx].toInt()} ${pairingPartner.capitalize()}"
        }
    }

}
