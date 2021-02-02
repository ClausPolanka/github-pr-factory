package pullrequestfactory.io.programs.impl

class FileAppProperties(fileName: String) {

    private val defaultUrl = "http://localhost"
    private val props = java.util.Properties()

    init {
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    fun getGithubBaseUrl() = props.getProperty("baseUrl", defaultUrl)
    fun getProjectVersion() = props.getProperty("projectVersion", defaultUrl)
    fun getGithubRepositoryPath() = props.getProperty("repoPath", defaultUrl)

}
