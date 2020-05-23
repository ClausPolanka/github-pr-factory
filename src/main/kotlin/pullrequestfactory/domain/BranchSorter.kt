package pullrequestfactory.domain

class BranchSorter {

    fun sort_branches(branches: List<Branch>, order: List<String>): List<Branch> {
        val sortedBranches = mutableListOf<List<Branch>>()
        order.forEach { o ->
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
