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
    LUKASL("Lukasl"),
    JOSEF("Josef"),
    JAROMIR("Jaromir"),
    VACLAV("Vaclav"),
    ALPER("Alper"),
    ALI("Ali"),
    IVAN("Ivan"),
    BEN("Benjamin", "Ben"),
    DAVID("David"),
    MARTON("Marton"),
    ;

    fun contains(branchName: String): Boolean = pullRequestNames.contains(branchName.replaceFirstChar { it.titlecase() })

    fun pullRequestName() = name.lowercase().replaceFirstChar { it.titlecase() }

    companion object {

        fun from(value: String) =
            values().find { it.pullRequestNames.contains(value.replaceFirstChar { c -> c.titlecase() }) }

        fun from(ordinal: Int) = try {
            val pp = values()[ordinal]
            pp
        } catch (e: Exception) {
            null
        }

        fun indexedNames(): List<String> =
            values().map { "${it.pullRequestName()} (${it.ordinal + 1})" }
    }
}
