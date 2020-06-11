package pullrequestfactory.io

class Properties(private val fileName: String) {

    private val defaultUrl = "http://localhost"

    fun getBaseUrl(): String {
        val props = java.util.Properties()
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
        return props.getProperty("baseUrl", defaultUrl)
    }

}
