package pullrequestfactory.io.programs.impl

import pullrequestfactory.io.programs.UserProperties

class FileUserProperties(fileName: String) : UserProperties {

    private val props = java.util.Properties()

    init {
        val propFile = this::class.java.classLoader.getResourceAsStream(fileName)
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
