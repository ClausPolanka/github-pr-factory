package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.UserProperties
import java.io.File
import java.io.FileNotFoundException

class FileUserProperties(fileName: String) : UserProperties {

    private val props = java.util.Properties()

    init {
        var propFile = this::class.java.classLoader.getResourceAsStream(fileName)
        if (propFile == null) {
            propFile = try {
                val file = File(fileName)
                file.inputStream()
            } catch (ex: NullPointerException) {
                null
            } catch (ex: FileNotFoundException) {
                null
            }
        }
        if (propFile != null) {
            props.load(propFile)
        }
    }

    override fun get_github_basic_auth_token(): String? {
        val token = props.getProperty("githubBasicAuthToken")
        if (token.isNullOrEmpty()) {
            return null
        }
        return token
    }

}
