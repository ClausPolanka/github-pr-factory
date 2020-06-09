package pullrequestfactory.domain

data class Branch(val name: String) {

    fun parts() = name.split("_")

}

