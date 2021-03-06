package pullrequestfactory.domain.branches

data class Branch(val name: String) {

    fun iterationNr(): Int = parts().dropLast(1).last().toInt()

    fun parts() = name.split("_")

    fun pairingPartner(): String = parts().last()

}

