package pullrequestfactory.domain

class BranchSorter {

    fun sort_branches_by_pairing_partner(branches: List<Branch>, orderByPairingPartner: List<String>): List<Branch> {
        val sortedBranches = mutableListOf<List<Branch>>()
        orderByPairingPartner.forEach { pairingPartner ->
            sortedBranches.add(sort(branches, pairingPartner))
        }
        return sortedBranches.flatten()
    }

    private fun sort(branches: List<Branch>, pairingPartner: String): List<Branch> {
        return branches
                .filter { it.name.endsWith(pairingPartner.toLowerCase()) }
                .map { branch ->
                    val branchNameParts = branch.name.split("_")
                    val iteration = branchNameParts.dropLast(1).last()
                    Pair(branch.name, iteration)
                }
                .sortedBy { it.second }
                .map { Branch(it.first) }
    }

}
