package pullrequestfactory.io

import java.io.FileInputStream
import java.nio.file.Paths

class Properties(private val fileName: String) {

    private val defaultUrl = "http://localhost"

    fun getBaseUrl(): String {
        val url = this::class.java.classLoader.getResource(fileName) ?: return defaultUrl
        val props = java.util.Properties()
        val path = Paths.get(url.toURI()).toString()
        props.load(FileInputStream(path))
        return props.getProperty("baseUrl", defaultUrl)
    }

}
