package pullrequestfactory.domain.uis

class QuietUI : UI {

    override fun show(msg: String) {
        // Do nothing here
    }

    override fun get_user_input(msg: String): String {
        // Do nothing here
        return ""
    }

}

