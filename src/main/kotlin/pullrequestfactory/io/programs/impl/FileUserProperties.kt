package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.UserProperties

class FileUserProperties(fileName: String) : UserProperties {

    private val props = java.util.Properties()

    init {
        props.load(this::class.java.classLoader.getResourceAsStream(fileName))
    }

    override fun get_github_basic_auth_token(): String? {
        val token = props.getProperty("githubBasicAuthToken")
        if (token.isEmpty()) {
            return null
        }
        return token
    }

}
