package pullrequestfactory.domain

data class Branch(val name: String) {

    fun parts() = name.split("_")

    fun iteration_nr(): Int = name.split("_").dropLast(1).last().toInt()

    fun pairing_partner(): String = parts().last()

}

