package pullrequestfactory

import com.github.ajalt.clikt.core.subcommands
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import pullrequestfactory.io.clikt.CloseCommand
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.clikt.GitHubPrFactoryCommand
import pullrequestfactory.io.clikt.OpenCommand
import pullrequestfactory.io.programs.impl.FileAppProperties
import pullrequestfactory.io.programs.impl.InstantSerializer
import pullrequestfactory.io.uis.ConsoleUI

fun main(args: Array<String>) {
    val appProps = FileAppProperties("app.properties")
    val cmdArgs = CommandArgs(
        appProps.getGithubBaseUrl(),
        appProps.getGithubRepositoryPath(),
        "user.properties",
        ConsoleUI(),
        Json { serializersModule = SerializersModule { contextual(InstantSerializer) } }
    )
    GitHubPrFactoryCommand(appProps.getProjectVersion())
        .subcommands(OpenCommand(cmdArgs), CloseCommand(cmdArgs))
        .main(args)
}
