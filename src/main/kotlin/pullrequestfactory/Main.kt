package pullrequestfactory

import com.github.ajalt.clikt.core.subcommands
import pullrequestfactory.io.clikt.CloseCommand
import pullrequestfactory.io.clikt.GitHubPrFactoryCommand
import pullrequestfactory.io.clikt.OpenCommand
import pullrequestfactory.io.programs.impl.FileAppProperties

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val baseUrl = appProps.get_github_base_url()
    val repoPath = appProps.get_github_repository_path()
    GitHubPrFactoryCommand(appProps.get_project_version())
            .subcommands(OpenCommand(baseUrl, repoPath), CloseCommand(baseUrl, repoPath))
            .main(args)
}
