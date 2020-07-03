package pullrequestfactory.io

import pullrequestfactory.domain.Candidate

private const val candidateOption = "-c"
private const val githubBasicAuthTokenOption = "-g"
private const val pairingPartnerOption = "-p"
private const val closeCommand = "close"
private const val openCommand = "open"

class ProgramArgs(private val args: Array<String>) {

    fun has_help_option(): Boolean =
            args.isEmpty() || args.size == 1 && args[0] == "-?" || args[0] == "--help"

    fun has_version_option(): Boolean =
            args.size == 1 && args[0] == "-v" || args[0] == "--version"

    fun has_open_command_help_option(): Boolean =
            args[0] == openCommand && args.size == 2 && args[1] == "--help"

    fun has_invalid_open_command(): Boolean = has_open_command() && !has_open_command_required_options()

    fun has_open_command(): Boolean = args[0] == openCommand

    private fun has_open_command_required_options() = args.size == 7
            && args.contains(candidateOption)
            && args.contains(githubBasicAuthTokenOption)
            && args.contains(pairingPartnerOption)

    fun has_close_command_help_option(): Boolean = args[0] == closeCommand
            && args.size == 2
            && args[1] == "--help"

    fun has_invalid_close_command(): Boolean = has_close_command() && !has_close_command_required_options()

    fun has_close_command(): Boolean = args[0] == closeCommand

    private fun has_close_command_required_options() = args.size == 5
            && args.contains(candidateOption)
            && args.contains(githubBasicAuthTokenOption)

    fun get_candidate(): Candidate {
        if (!is_candidate_syntax_valid()) {
            throw WrongCandidateArgumentSyntax("Either option -c or candidate missing or candidate has wrong format")
        }
        val candidateArg = args[args.indexOf(candidateOption) + 1]
        val firstName = candidateArg.split("-")[0]
        val lastName = candidateArg.split("-")[1]
        return Candidate(firstName, lastName)
    }

    private fun is_candidate_syntax_valid() = args.size >= 2
            && args.contains(candidateOption)
            && args[args.indexOf(candidateOption) + 1].contains("-")

    fun get_github_basic_auth_token(): String {
        if (!is_github_basic_auth_token_syntax_valid()) {
            throw WrongGithubBasicAuthTokenArgumentSyntax("Either option -g or github basic auth token missing")
        }
        val indexOfGithubToken = args.indexOf(githubBasicAuthTokenOption) + 1
        return args[indexOfGithubToken]
    }

    private fun is_github_basic_auth_token_syntax_valid(): Boolean = args.size >= 2
            && args.contains(githubBasicAuthTokenOption)

    fun get_pairing_partner(): List<String> {
        val indexOfPairingPartner = args.indexOf(pairingPartnerOption) + 1
        return args[indexOfPairingPartner].split("-")
    }

}

class WrongCandidateArgumentSyntax(msg: String) : RuntimeException(msg)
class WrongGithubBasicAuthTokenArgumentSyntax(msg: String) : RuntimeException(msg)
