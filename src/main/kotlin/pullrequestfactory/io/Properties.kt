package pullrequestfactory.io

class Properties(private val fileName: String) {

    private val defaultUrl = "http://localhost"

    fun get_base_url(): String {
        val props = java.util.Properties()
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
        return props.getProperty("baseUrl", defaultUrl)
    }

    fun get_project_version(): String {
        val props = java.util.Properties()
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
        return props.getProperty("projectVersion", defaultUrl)
    }

}
