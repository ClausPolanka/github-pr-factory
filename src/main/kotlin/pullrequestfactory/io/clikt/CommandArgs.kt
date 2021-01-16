package pullrequestfactory.io.clikt

class CommandArgs(
        val baseUrl: String,
        repoPath: String,
        val userPropertiesFile: String
) {
    val repoUrl = baseUrl + repoPath
}
