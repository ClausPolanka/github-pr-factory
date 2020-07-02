package pullrequestfactory.io

class Properties(fileName: String) {

    private val props = java.util.Properties()
    private val defaultUrl = "http://localhost"

    init {
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    fun get_base_url(): String {
        return props.getProperty("baseUrl", defaultUrl)
    }

    fun get_project_version(): String {
        return props.getProperty("projectVersion", defaultUrl)
    }

}
