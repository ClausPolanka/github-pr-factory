package pullrequestfactory

import com.github.ajalt.clikt.core.subcommands
import pullrequestfactory.io.clikt.CloseCommand
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.clikt.GitHubPrFactoryCommand
import pullrequestfactory.io.clikt.OpenCommand
import pullrequestfactory.io.programs.impl.FileAppProperties

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val cmdArgs = CommandArgs(
            appProps.get_github_base_url(),
            appProps.get_github_repository_path(),
            "user.properties")
    GitHubPrFactoryCommand(appProps.get_project_version())
            .subcommands(OpenCommand(cmdArgs), CloseCommand(cmdArgs))
            .main(args)
}
