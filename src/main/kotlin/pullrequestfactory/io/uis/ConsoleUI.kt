package pullrequestfactory.io.uis

import pullrequestfactory.domain.uis.UI

class ConsoleUI : UI {

    override fun show(msg: String) {
        println(msg)
    }

    override fun get_user_input(msg: String): String {
        var userInput: String? = null
        while (userInput.isNullOrEmpty()) {
            print(msg)
            userInput = readLine()
        }
        return userInput
    }
}
