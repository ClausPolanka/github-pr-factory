package pullrequestfactory.domain

class Sessions(private val branches: List<Branch>) {

    fun create(): List<String> {
        var prevSession = 1
        return branches.mapIndexed { idx, branch ->
            when (idx) {
                0 -> prevSession.toString()
                else -> {
                    val (_, _, _, _, currPP) = branch.parts()
                    val (_, _, _, _, prevPP) = branches[idx - 1].parts()
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

