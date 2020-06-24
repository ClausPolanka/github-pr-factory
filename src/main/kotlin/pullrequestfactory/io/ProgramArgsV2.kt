package pullrequestfactory.io

class ProgramArgsV2(private val args: Array<String>) {

    fun has_help_option(): Boolean =
            args.isEmpty() || args.size == 1 && args[0] == "-?" || args[0] == "--help"

    fun has_open_command_help_option(): Boolean =
            args[0] == "open" && args.size == 2 && args[1] == "--help"

    fun has_invalid_open_command(): Boolean = has_open_command() && !has_open_command_required_options()

    fun has_open_command(): Boolean = args[0] == "open"

    private fun has_open_command_required_options() = args.size == 7
            && args.contains("-c")
            && args.contains("-g")
            && args.contains("-p")

    fun has_close_command_help_option(): Boolean = args[0] == "close"
            && args.size == 2
            && args[1] == "--help"

    fun has_invalid_close_command(): Boolean = has_close_command() && !has_close_command_required_options()

    fun has_close_command(): Boolean = args[0] == "close"

    private fun has_close_command_required_options() = args.size == 5
            && args.contains("-c")
            && args.contains("-g")

}
