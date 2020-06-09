package pullrequestfactory.domain

class HeadBranches(private val branches: List<Branch>) {

    fun create(): List<String> {
        return branches.map { it.name }
    }

}
