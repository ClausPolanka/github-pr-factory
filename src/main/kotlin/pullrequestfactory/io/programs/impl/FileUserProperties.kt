package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.UserProperties
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

class FileUserProperties(fileName: String) : UserProperties {

    private val props = java.util.Properties()

    init {
        var propFile = this::class.java.classLoader.getResourceAsStream(fileName)
        if (propFile == null) {
            propFile = read_from_file_system(fileName)
        }
        if (propFile != null) {
            props.load(propFile)
        }
    }

    private fun read_from_file_system(fileName: String): InputStream? {
        return try {
            val file = File(fileName)
            file.inputStream()
        } catch (ex: FileNotFoundException) {
            null
        }
    }

    override fun get_github_auth_token(): String? {
        val token = props.getProperty("githubAuthToken")
        if (token.isNullOrEmpty()) {
            return null
        }
        return token
    }

}
