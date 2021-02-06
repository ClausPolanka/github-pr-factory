package pullrequestfactory.io.clikt

import pullrequestfactory.domain.uis.UI

class CommandArgs(
    val baseUrl: String,
    repoPath: String,
    val userPropertiesFile: String,
    val ui: UI
) {
    val repoUrl = baseUrl + repoPath
}
