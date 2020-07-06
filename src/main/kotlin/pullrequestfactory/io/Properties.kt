package pullrequestfactory.io

class Properties(fileName: String) {

    private val props = java.util.Properties()
    private val defaultUrl = "http://localhost"

    init {
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    fun get_github_base_url() = props.getProperty("baseUrl", defaultUrl)

    fun get_project_version() = props.getProperty("projectVersion", defaultUrl)

    fun get_github_repository_path() = props.getProperty("repoPath", defaultUrl)

}
