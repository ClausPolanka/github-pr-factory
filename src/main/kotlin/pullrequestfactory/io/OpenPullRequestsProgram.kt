package pullrequestfactory.io

import pullrequestfactory.domain.*

class OpenPullRequestsProgram(private val programArgs: ProgramArgs) : Program {

    override fun execute() {
        val candidate = programArgs.get_candidate()
        val githubBasicAuthToken = programArgs.get_github_basic_auth_token()
        val pairingPartner = programArgs.get_pairing_partner()
        val baseUrl = Properties("app.properties").get_base_url()
        val repoPath = Properties("app.properties").get_github_repository_path()
        val githubRepo = GithubHttpRepo(
                baseUrl,
                repoPath,
                githubBasicAuthToken,
                NoopCache(),
                QuietUI())
        val f = GithubPRFactory(githubRepo, githubRepo, BranchSyntaxValidator(ConsoleUI()))
        f.create_pull_requests(candidate, pairingPartner)
    }

}
