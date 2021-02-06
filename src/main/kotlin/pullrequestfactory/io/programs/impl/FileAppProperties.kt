package pullrequestfactory.io.programs.impl

class FileAppProperties(fileName: String) {

    private val defaultUrl = "http://localhost"
    private val props = java.util.Properties()

    init {
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    fun getGithubBaseUrl(): String = props.getProperty("baseUrl", defaultUrl)
    fun getProjectVersion(): String = props.getProperty("projectVersion", defaultUrl)
    fun getGithubRepositoryPath(): String = props.getProperty("repoPath", defaultUrl)

}
