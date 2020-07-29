package pullrequestfactory.domain.branches

data class Branch(val name: String) {

    fun iteration_nr(): Int = parts().dropLast(1).last().toInt()

    fun parts() = name.split("_")

    fun pairing_partner(): String = parts().last()

}

