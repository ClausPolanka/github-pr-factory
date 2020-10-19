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

        fun value_of(value: String): PairingPartner? {
            val pps = values().find { it.pullRequestNames.contains(value.capitalize()) }
            return pps
        }

        fun value_of(ordinal: Int): PairingPartner? {
            return try {
                val pp = values()[ordinal]
                pp
            } catch (e: Exception) {
                null
            }
        }

        fun indexed_names(): List<String> {
            return values().map { "${it.pull_request_name()} (${it.ordinal + 1})" }.toList()
        }

    }

}
