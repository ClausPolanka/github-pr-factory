package ut.pullrequestfactory.domain.branches

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.BranchSorter

class BranchSorterTest {

    private val PAIRING_PARTNER = "pairingpartner"

    @Test
    fun sorts_unsorted_branches_by_iteration_number_for_the_same_pairing_partner() {
        val sut = BranchSorter()

        val sortedBranches = sut.sort_branches_by_pairing_partner(listOf(
                branch_for(PAIRING_PARTNER, iterationNr = 2),
                branch_for(PAIRING_PARTNER, iterationNr = 1)),
                pairingPartner = listOf(PAIRING_PARTNER))

        assertThat(sortedBranches).containsExactly(
                branch_for(PAIRING_PARTNER, iterationNr = 1),
                branch_for(PAIRING_PARTNER, iterationNr = 2))
    }

    @Test
    fun sorts_unsorted_branches_by_pairing_partner_for_the_same_iteration_number() {
        val sut = BranchSorter()

        val sortedBranches = sut.sort_branches_by_pairing_partner(listOf(
                branch_for(PAIRING_PARTNER + 2, iterationNr = 1),
                branch_for(PAIRING_PARTNER + 1, iterationNr = 1)),
                pairingPartner = listOf(PAIRING_PARTNER + 1, PAIRING_PARTNER + 2))

        assertThat(sortedBranches).containsExactly(
                branch_for(PAIRING_PARTNER + 1, iterationNr = 1),
                branch_for(PAIRING_PARTNER + 2, iterationNr = 1))
    }

    @Test
    fun sorts_unsorted_branches_by_iteration_number_for_the_same_pairing_partner_ignoring_case() {
        val sut = BranchSorter()

        val sortedBranches = sut.sort_branches_by_pairing_partner(listOf(
                branch_for(PAIRING_PARTNER, iterationNr = 2),
                branch_for(PAIRING_PARTNER, iterationNr = 1)),
                pairingPartner = listOf(PAIRING_PARTNER.capitalize()))

        assertThat(sortedBranches).containsExactly(
                branch_for(PAIRING_PARTNER, iterationNr = 1),
                branch_for(PAIRING_PARTNER, iterationNr = 2))
    }

    private fun branch_for(pairingPartner: String, iterationNr: Int) =
            Branch("firstname_lastname_iteration_${iterationNr}_$pairingPartner")

}
