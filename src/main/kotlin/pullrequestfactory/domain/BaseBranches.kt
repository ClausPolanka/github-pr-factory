package pullrequestfactory.domain

class BaseBranches(private val branches: List<Branch>) {

    fun create(): List<String> {
        return branches.mapIndexed { idx, br ->
            when (idx) {
                0 -> "master"
                else -> branches[idx.dec()].name
            }
        }
    }

}
