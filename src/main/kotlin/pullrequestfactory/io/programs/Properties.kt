package pullrequestfactory.io.programs

interface Properties {
    fun get_github_base_url(): String?
    fun get_project_version(): String?
    fun get_github_repository_path(): String?
    fun get_github_basic_auth_token(): String?
}
