package pullrequestfactory.domain

class Sessions(private val branches: List<Branch>) {

    fun create(): List<String> {
        var prevSession = 1
        return branches.mapIndexed { idx, branch ->
            when (idx) {
                0 -> prevSession.toString()
                else -> {
                    val (_, _, _, currIter, currPP) = branch.name.split("_")
                    val (_, _, _, prevIter, prevPP) = branches[idx - 1].name.split("_")
                    if (prevPP == currPP) {
                        "$prevSession"
                    } else {
                        prevSession = prevSession.inc()
                        "$prevSession"
                    }
                }
            }
        }
    }

}
