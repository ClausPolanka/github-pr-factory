package pullrequestfactory.io.uis

import pullrequestfactory.domain.uis.UI

class ConsoleUI : UI {

    override fun show(msg: String) {
        println(msg)
    }

}
