package pullrequestfactory.domain

class BranchSorter {

    fun sort_branches_by_pairing_partner(branches: List<Branch>, orderByPairingPartner: List<String>): List<Branch> {
        return orderByPairingPartner.map { pairingPartner ->
            create_sorted_branches(branches, pairingPartner)
        }.flatten()
    }

    private fun create_sorted_branches(branches: List<Branch>, pairingPartner: String): List<Branch> {
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
