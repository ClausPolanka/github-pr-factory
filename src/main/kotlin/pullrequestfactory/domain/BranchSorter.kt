package pullrequestfactory.domain

class BranchSorter {

    fun sort_branches_by_pairing_partner(branches: List<Branch>, pairingPartner: List<String>): List<Branch> {
        return pairingPartner.map {
            create_sorted_branches(branches, it)
        }.flatten()
    }

    private fun create_sorted_branches(branches: List<Branch>, pairingPartner: String): List<Branch> {
        val branchesForPairingPartner = branches.filter { it.name.endsWith(pairingPartner.toLowerCase()) }
        return sort_by_iteration_number(branchesForPairingPartner)
    }

    private fun sort_by_iteration_number(branches: List<Branch>): List<Branch> {
        return branches
                .map { branch ->
                    val branchNameParts = branch.name.split("_")
                    val iteration = branchNameParts.dropLast(1).last()
                    Pair(branch.name, iteration)
                }
                .sortedBy { it.second }
                .map { Branch(it.first) }
    }

}
