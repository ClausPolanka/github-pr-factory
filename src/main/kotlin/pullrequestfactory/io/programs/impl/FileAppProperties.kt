package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.AppProperties

class FileAppProperties(fileName: String) : AppProperties {

    private val DEFAULT_URL = "http://localhost"
    private val props = java.util.Properties()

    init {
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    override fun get_github_base_url() = props.getProperty("baseUrl", DEFAULT_URL)

    override fun get_project_version() = props.getProperty("projectVersion", DEFAULT_URL)

    override fun get_github_repository_path() = props.getProperty("repoPath", DEFAULT_URL)

}
