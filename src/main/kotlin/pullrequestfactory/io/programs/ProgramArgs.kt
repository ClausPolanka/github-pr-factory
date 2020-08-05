package pullrequestfactory.io.programs

import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner

class ProgramArgs(private val args: Array<String>) {

    private val HELP_COMMAND = "-?"
    private val HELP_COMMAND_LONG_VERSION = "--help"
    private val VERSION_COMMAND = "-v"
    private val VERSION_COMMAND_LONG_VERSION = "--version"
    private val CANDIDATE_OPTION = "-c"
    private val GITHUB_BASIC_AUTH_TOKEN_OPTION = "-g"
    private val PAIRING_PARTNER_OPTION = "-p"
    private val CLOSE_COMMAND = "close"
    private val OPEN_COMMAND = "open"
    private val IS_LAST_PULL_REQUEST_FINISHED = "-l"
    private val IS_LAST_PULL_REQUEST_FINISHED_LONG_VERSION = "--last-finished"
    private val ERROR_MSG_CANDIDATE = "Either option -c or candidate missing or candidate has wrong format"
    private val ERROR_MSG_PAIRING_PARTNER = "Either option -p or pairing partner are missing or pairing partner have wrong format or is unknown"
    private val ERROR_MSG_TOKEN = "Either option -g or github basic auth token missing"

    fun has_help_option() =
            args.isEmpty() || args.size == 1 && (args[0] == HELP_COMMAND || args[0] == HELP_COMMAND_LONG_VERSION)

    fun has_version_option() =
            args.size == 1 && (args[0] == VERSION_COMMAND || args[0] == VERSION_COMMAND_LONG_VERSION)

    fun has_open_command_help_option() =
            args[0] == OPEN_COMMAND && args.size == 2 && args[1] == HELP_COMMAND_LONG_VERSION

    fun has_invalid_open_command() = has_open_command()
            && !((args.size == 7 && has_open_command_required_options()) || (args.size == 8 && has_open_command_required_and_optional_options()))

    fun has_open_command() = args[0] == OPEN_COMMAND

    fun has_open_command_with_optional_options() = args[0] == OPEN_COMMAND && is_last_pull_request_finished()

    private fun has_open_command_required_options() = is_candidate_syntax_valid()
            && is_github_basic_auth_token_syntax_valid()
            && is_pairing_partner_syntax_valid()

    private fun has_open_command_required_and_optional_options() =
            has_open_command_required_options() && is_last_pull_request_finished()

    fun has_close_command_help_option() =
            args[0] == CLOSE_COMMAND && args.size == 2 && args[1] == HELP_COMMAND_LONG_VERSION

    fun has_invalid_close_command() = has_close_command() && !(has_close_command_required_options() || is_interactive_mode())

    fun has_close_command() = args[0] == CLOSE_COMMAND

    private fun has_close_command_required_options() = args.size == 5
            && is_candidate_syntax_valid()
            && is_github_basic_auth_token_syntax_valid()

    private fun is_interactive_mode() = args.contains("-i") || args.contains("--interactive")

    fun get_candidate(): Candidate {
        validate_args_candidate_syntax()
        val candidateArg = args[args.indexOf(CANDIDATE_OPTION) + 1]
        val firstName = candidateArg.split("-")[0]
        val lastName = candidateArg.split("-")[1]
        return Candidate(firstName, lastName)
    }

    private fun validate_args_candidate_syntax() {
        if (!is_candidate_syntax_valid()) {
            throw WrongCandidateArgumentSyntax(ERROR_MSG_CANDIDATE)
        }
    }

    private fun is_candidate_syntax_valid() = args.size >= args.indexOf(CANDIDATE_OPTION) + 1 /*candidate */ + 1 /* since array is 0-based */
            && args.contains(CANDIDATE_OPTION)
            && args[args.indexOf(CANDIDATE_OPTION) + 1].contains("-")

    fun get_github_basic_auth_token(): String {
        validate_args_token_syntax()
        val indexOfGithubToken = args.indexOf(GITHUB_BASIC_AUTH_TOKEN_OPTION) + 1
        return args[indexOfGithubToken]
    }

    private fun validate_args_token_syntax() {
        if (!is_github_basic_auth_token_syntax_valid()) {
            throw WrongGithubBasicAuthTokenArgumentSyntax(ERROR_MSG_TOKEN)
        }
    }

    private fun is_github_basic_auth_token_syntax_valid() =
            args.size >= args.indexOf(GITHUB_BASIC_AUTH_TOKEN_OPTION) + 1 /* token */ + 1 /* since array is 0-based */
                    && args.contains(GITHUB_BASIC_AUTH_TOKEN_OPTION)

    fun get_pairing_partner(): List<PairingPartner> {
        validate_args_pairing_partner_syntax()
        val pairingPartner = args[args.indexOf(PAIRING_PARTNER_OPTION) + 1]
        return create_pairing_partner(pairingPartner)
    }

    private fun validate_args_pairing_partner_syntax() {
        if (!is_pairing_partner_syntax_valid()) {
            throw WrongPairingPartnerArgumentSyntax(ERROR_MSG_PAIRING_PARTNER)
        }
    }

    private fun create_pairing_partner(pairingPartner: String): List<PairingPartner> {
        return try {
            pairingPartner.split("-").map { PairingPartner.valueOf(it.toUpperCase()) }
        } catch (e: IllegalArgumentException) {
            throw WrongPairingPartnerArgumentSyntax("$ERROR_MSG_PAIRING_PARTNER ${e.message}")
        }
    }

    private fun is_pairing_partner_syntax_valid() =
            args.size >= args.indexOf(PAIRING_PARTNER_OPTION) + 1 /* pairing partner */ + 1 /* since array is 0-based */
                    && args.contains(PAIRING_PARTNER_OPTION)
                    && args[args.indexOf(PAIRING_PARTNER_OPTION) + 1].contains("-")
                    && args[args.indexOf(PAIRING_PARTNER_OPTION) + 1].split("-").size == 7

    private fun is_last_pull_request_finished() =
            args.contains(IS_LAST_PULL_REQUEST_FINISHED) || args.contains(IS_LAST_PULL_REQUEST_FINISHED_LONG_VERSION)

    fun has_close_command_in_interactive_mode(): Boolean {
        return is_interactive_mode()
    }

    class WrongCandidateArgumentSyntax(msg: String) : RuntimeException(msg)
    class WrongGithubBasicAuthTokenArgumentSyntax(msg: String) : RuntimeException(msg)
    class WrongPairingPartnerArgumentSyntax(msg: String) : RuntimeException(msg)

}
