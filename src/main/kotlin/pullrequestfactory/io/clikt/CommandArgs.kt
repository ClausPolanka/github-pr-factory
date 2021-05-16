package pullrequestfactory.io.clikt

import kotlinx.serialization.json.Json
import pullrequestfactory.domain.uis.UI

class CommandArgs(
    val baseUrl: String,
    repoPath: String,
    val userPropertiesFile: String,
    val ui: UI,
    val jsonSerizalizer: Json
) {
    val repoUrl = baseUrl + repoPath
}
