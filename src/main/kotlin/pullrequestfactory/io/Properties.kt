package pullrequestfactory.io

class Properties(private val fileName: String) {

    private val indexOfValue = 1
    private val defaultUrl = "http://localhost"

    fun getBaseUrl(): String {
        val fileContent = this::class.java.classLoader.getResource(fileName)?.readText()
        return fileContent?.split("\n")
                ?.filter { it.isNotEmpty() }
                ?.first { it.startsWith("baseUrl") }
                ?.split("=")?.get(indexOfValue) ?: defaultUrl
    }

}
