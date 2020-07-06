package pullrequestfactory.io

class Properties(fileName: String) {

    private val DEFAULT_URL = "http://localhost"
    private val props = java.util.Properties()

    init {
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    fun get_github_base_url() = props.getProperty("baseUrl", DEFAULT_URL)

    fun get_project_version() = props.getProperty("projectVersion", DEFAULT_URL)

    fun get_github_repository_path() = props.getProperty("repoPath", DEFAULT_URL)

}
