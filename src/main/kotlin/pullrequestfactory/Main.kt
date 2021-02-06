package pullrequestfactory

import com.github.ajalt.clikt.core.subcommands
import pullrequestfactory.io.clikt.CloseCommand
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.clikt.GitHubPrFactoryCommand
import pullrequestfactory.io.clikt.OpenCommand
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.uis.ConsoleUI

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val cmdArgs = CommandArgs(
        appProps.getGithubBaseUrl(),
        appProps.getGithubRepositoryPath(),
        "user.properties",
        ConsoleUI()
    )
    GitHubPrFactoryCommand(appProps.getProjectVersion())
        .subcommands(OpenCommand(cmdArgs), CloseCommand(cmdArgs))
        .main(args)
}
