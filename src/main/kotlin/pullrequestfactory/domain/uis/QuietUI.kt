package pullrequestfactory.domain.uis

class QuietUI : UI {

    override fun show(msg: String) {
        // Do nothing here
    }

    override fun getUserInput(msg: String): String {
        // Do nothing here
        return ""
    }

}

