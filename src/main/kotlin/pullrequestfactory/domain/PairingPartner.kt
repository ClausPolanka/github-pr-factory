package pullrequestfactory.domain

enum class PairingPartner(vararg val pullRequestNames: String) {
    ANDREJ("Andrej"),
    SHUBI("Shubi"),
    CLAUS("Claus"),
    BERNHARD("Bernhard", "Berni"),
    DOMINIK("Dominik", "Moser"),
    MIHAI("Mihai"),
    NANDOR("Nandor"),
    CHRISTIAN("Christian"),
    TOMAS("Tomas", "Tomasr"),
    LAMPE("Lampe"),
    MARKUS("Markus"),
    JOHN("John"),
    MARTIN("Martin"),
    PETER("Peter"),
    TIBOR("Tibor"),
    JAKUB("Jakub"),
    Lukas("Lukas");

    fun contains(branchName: String): Boolean {
        return pullRequestNames.contains(branchName.capitalize())
    }

    companion object {

        fun value_of(value: String): PairingPartner {
            val pp = values().find { it.pullRequestNames.contains(value.capitalize()) }
            return when (pp) {
                null -> throw IllegalArgumentException("Pairing partner branch name is invalid: '$value'")
                else -> pp
            }
        }

    }

}
