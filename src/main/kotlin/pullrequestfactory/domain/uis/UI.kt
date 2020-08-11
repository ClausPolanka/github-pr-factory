package pullrequestfactory.domain.uis

interface UI {

    fun show(msg: String)

    fun get_user_input(msg: String): String

}
