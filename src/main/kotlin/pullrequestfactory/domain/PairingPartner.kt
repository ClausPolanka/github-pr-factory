package pullrequestfactory.domain

enum class PairingPartner(vararg val pullRequestNames: String) {
    ANDREJ("Andrej"),
    SHUBHI("Shubhi", "Shubi", "Joshi"),
    CLAUS("Claus"),
    BERNI("Bernhard", "Berni"),
    DOMINIK("Dominik", "Moser"),
    MIHAI("Mihai"),
    MICHAL("Michal"),
    NANDOR("Nandor"),
    CHRISTIAN("Christian"),
    TOMAS("Tomas", "Tomasr"),
    LAMPE("Lampe", "Patrick"),
    MARKUS("Markus"),
    JOHN("John"),
    MARTIN("Martin"),
    PETER("Peter"),
    TIBOR("Tibor"),
    JAKUB("Jakub"),
    LUKAS("Lukas"),
    JOSEF("Josef"),
    JAROMIR("Jaromir");

    fun contains(branchName: String): Boolean {
        return pullRequestNames.contains(branchName.capitalize())
    }

    fun pull_request_name() = name.toLowerCase().capitalize()

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
