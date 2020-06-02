package pullrequestfactory.io

import java.nio.file.Files
import java.nio.file.Paths

class Properties(private val fileName: String) {

    private val indexOfValue = 1
    private val defaultUrl = "http://localhost"

    fun getBaseUrl(): String {
        val url = this::class.java.classLoader.getResource(fileName) ?: return defaultUrl
        return Files.readAllLines(Paths.get(url.toURI()))
                .first { it.startsWith("baseUrl") }
                .split("=")[indexOfValue]
    }

}
