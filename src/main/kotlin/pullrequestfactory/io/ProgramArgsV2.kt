package pullrequestfactory.io

class ProgramArgsV2(private val args: Array<String>) {

    fun hasHelpOption(): Boolean =
            args.isEmpty() || args[0] == "-?" || args[0] == "--help"

    fun hasOpenCommandHelpOption(): Boolean =
            args[0] == "open" && args[1] == "--help"

    fun hasOpenCommand(): Boolean = args[0] == "open"

    fun hasCloseCommandHelpOption(): Boolean =
            args[0] == "close" && args[1] == "--help"

    fun hasCloseCommand(): Boolean = args[0] == "close"

}
