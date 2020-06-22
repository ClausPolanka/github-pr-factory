package pullrequestfactory.io

class ProgramArgsV2(private val args: Array<String>) {

    fun hasHelpOption(): Boolean =
            args.isEmpty() || args.size == 1 && args[0] == "-?" || args[0] == "--help"

    fun hasOpenCommandHelpOption(): Boolean =
            args[0] == "open" && args.size == 2 && args[1] == "--help"

    fun hasInvalidOpenCommand(): Boolean = hasOpenCommand() && args.size != 7

    fun hasOpenCommand(): Boolean = args[0] == "open"

    fun hasCloseCommandHelpOption(): Boolean =
            args[0] == "close" && args.size == 2 && args[1] == "--help"

    fun hasInvalidCloseCommand(): Boolean = hasCloseCommand() && args.size != 5

    fun hasCloseCommand(): Boolean = args[0] == "close"

}
