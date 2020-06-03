package pullrequestfactory.io

import pullrequestfactory.domain.UI
import java.nio.file.Files
import java.nio.file.Paths

class Properties(private val fileName: String, private val ui: UI) {

    private val indexOfValue = 1
    private val defaultUrl = "http://localhost"

    fun getBaseUrl(): String {
        val url = this::class.java.classLoader.getResource(fileName) ?: return defaultUrl
        val property = Files.readAllLines(Paths.get(url.toURI()))
                .find { it.startsWith("baseUrl") }
                .orEmpty()
        return if (property.isEmpty()) {
            ui.show("[WARNING] baseUrl property not found therefore default url will be used")
            defaultUrl
        } else {
            property.split("=")[indexOfValue]
        }
    }

}
