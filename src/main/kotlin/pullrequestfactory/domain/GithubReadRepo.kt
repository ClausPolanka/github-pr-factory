package pullrequestfactory.domain

interface GithubReadRepo {
    fun get_all_branches(): List<Branch>
}
