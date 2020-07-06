package pullrequestfactory.domain.branches

class BaseBranches(private val branches: List<Branch>) {

    fun create(): List<Branch> {
        return branches.mapIndexed { idx, br ->
            when (idx) {
                0 -> Branch("master")
                else -> branches[idx.dec()]
            }
        }
    }

}
