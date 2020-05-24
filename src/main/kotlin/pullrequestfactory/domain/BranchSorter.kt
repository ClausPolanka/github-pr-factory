package pullrequestfactory.domain

class BranchSorter {

    fun sort_branches_by_pairing_partner(branches: List<Branch>, orderByPairingPartner: List<String>): List<Branch> {
        val sortedBranches = mutableListOf<List<Branch>>()
        orderByPairingPartner.forEach { pairingPartner ->
            sortedBranches.add(branches
                    .filter { it.name.endsWith(pairingPartner.toLowerCase()) }
                    .map {
                        val branch_name_parts = it.name.split("_")
                        val iteration = branch_name_parts.dropLast(1).last()
                        Pair(it.name, iteration)
                    }
                    .sortedBy { it.second }
                    .map { Branch(it.first) })
        }
        return sortedBranches.flatten()
    }

}
