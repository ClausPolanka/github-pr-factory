package pullrequestfactory.domain

class BranchSorter {

    fun sort_branches(branches: List<Branch>, orderByPairingPartner: List<String>): List<Branch> {
        val sortedBranches = mutableListOf<List<Branch>>()
        orderByPairingPartner.forEach { o ->
            sortedBranches.add(branches
                    .filter { it.name.endsWith(o.toLowerCase()) }
                    .map {
                        val parts = it.name.split("_")
                        val iteration = parts[parts.size - 2]
                        Pair(it.name, iteration)
                    }
                    .sortedBy { it.second }
                    .map { Branch(it.first) })
        }
        return sortedBranches.flatten()
    }

}
