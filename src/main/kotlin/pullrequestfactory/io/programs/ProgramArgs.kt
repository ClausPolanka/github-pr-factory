package pullrequestfactory.io.programs

import pullrequestfactory.domain.Candidate


class ProgramArgs(private val args: Array<String>) {

    private val CANDIDATE_OPTION = "-c"
    private val GITHUB_BASIC_AUTH_TOKEN_OPTION = "-g"
    private val PAIRING_PARTNER_OPTION = "-p"
    private val CLOSE_COMMAND = "close"
    private val OPEN_COMMAND = "open"

    fun has_help_option() =
            args.isEmpty() || args.size == 1 && (args[0] == "-?" || args[0] == "--help")

    fun has_version_option() =
            args.size == 1 && (args[0] == "-v" || args[0] == "--version")

    fun has_open_command_help_option() =
            args[0] == OPEN_COMMAND && args.size == 2 && args[1] == "--help"

    fun has_invalid_open_command() =
            has_open_command() && !has_open_command_required_options()

    fun has_open_command() = args[0] == OPEN_COMMAND

    private fun has_open_command_required_options() = args.size == 7
            && is_candidate_syntax_valid()
            && is_github_basic_auth_token_syntax_valid()
            && is_pairing_partner_syntax_valid()

    fun has_close_command_help_option() =
            args[0] == CLOSE_COMMAND && args.size == 2 && args[1] == "--help"

    fun has_invalid_close_command() = has_close_command() && !has_close_command_required_options()

    fun has_close_command() = args[0] == CLOSE_COMMAND

    private fun has_close_command_required_options() = args.size == 5
            && is_candidate_syntax_valid()
            && is_github_basic_auth_token_syntax_valid()

    fun get_candidate(): Candidate {
        if (!is_candidate_syntax_valid()) {
            throw WrongCandidateArgumentSyntax("Either option -c or candidate missing or candidate has wrong format")
        }
        val candidateArg = args[args.indexOf(CANDIDATE_OPTION) + 1]
        val firstName = candidateArg.split("-")[0]
        val lastName = candidateArg.split("-")[1]
        return Candidate(firstName, lastName)
    }

    private fun is_candidate_syntax_valid() = args.size >= 2
            && args.contains(CANDIDATE_OPTION)
            && args[args.indexOf(CANDIDATE_OPTION) + 1].contains("-")

    fun get_github_basic_auth_token(): String {
        if (!is_github_basic_auth_token_syntax_valid()) {
            throw WrongGithubBasicAuthTokenArgumentSyntax("Either option -g or github basic auth token missing")
        }
        val indexOfGithubToken = args.indexOf(GITHUB_BASIC_AUTH_TOKEN_OPTION) + 1
        return args[indexOfGithubToken]
    }

    private fun is_github_basic_auth_token_syntax_valid() =
            args.size >= 2 && args.contains(GITHUB_BASIC_AUTH_TOKEN_OPTION)

    fun get_pairing_partner(): List<String> {
        if (!is_pairing_partner_syntax_valid()) {
            throw WrongPairingPartnerArgumentSyntax("Either option -p or pairing partner are missing or pairing partner have wrong format")
        }
        val indexOfPairingPartner = args.indexOf(PAIRING_PARTNER_OPTION) + 1
        return args[indexOfPairingPartner].split("-")
    }

    private fun is_pairing_partner_syntax_valid() = args.size >= 2
            && args.contains(PAIRING_PARTNER_OPTION)
            && args[args.indexOf(PAIRING_PARTNER_OPTION) + 1].contains("-")
            && args[args.indexOf(PAIRING_PARTNER_OPTION) + 1].split("-").size == 7

    private class WrongCandidateArgumentSyntax(msg: String) : RuntimeException(msg)
    private class WrongGithubBasicAuthTokenArgumentSyntax(msg: String) : RuntimeException(msg)
    private class WrongPairingPartnerArgumentSyntax(msg: String) : RuntimeException(msg)

}
