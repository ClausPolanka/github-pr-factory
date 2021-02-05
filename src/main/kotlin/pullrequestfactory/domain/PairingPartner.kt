package pullrequestfactory.domain

enum class PairingPartner(vararg val pullRequestNames: String) {
    ANDREJ("Andrej"),
    SHUBHI("Shubhi", "Shubi", "Joshi"),
    CLAUS("Claus"),
    BERNI("Bernhard", "Berni"),
    DOMINIK("Dominik", "Moser"),
    MIHAI("Mihai"),
    MICHAL("Michal"),
    NANDOR("Nandor", "Nandi"),
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
    JAROMIR("Jaromir"),
    VACLAV("Vaclav");

    fun contains(branchName: String): Boolean = pullRequestNames.contains(branchName.capitalize())

    fun pullRequestName() = name.toLowerCase().capitalize()

    companion object {

        fun value_of(value: String) =
            values().find { it.pullRequestNames.contains(value.capitalize()) }

        fun value_of(ordinal: Int) = try {
            val pp = values()[ordinal]
            pp
        } catch (e: Exception) {
            null
        }

        fun indexedNames(): List<String> =
            values().map { "${it.pullRequestName()} (${it.ordinal + 1})" }
    }
}
