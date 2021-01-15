package pullrequestfactory.io.clikt

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.versionOption

class GitHubPrFactoryCommand(appVersion: String) : CliktCommand(name = "github-pr-factory") {
    init {
        versionOption(appVersion)
    }

    override fun run() = Unit
}
