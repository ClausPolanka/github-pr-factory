package pullrequestfactory.io

import pullrequestfactory.domain.UI

class ConsoleUI : UI {

    override fun show(msg: String) {
        println(msg)
    }

}
