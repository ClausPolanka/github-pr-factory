package pullrequestfactory.domain.uis

interface UI {

    fun show(msg: String)

    fun getUserInput(msg: String): String

}
