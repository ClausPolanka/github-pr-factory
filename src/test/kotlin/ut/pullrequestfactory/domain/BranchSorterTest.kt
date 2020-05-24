package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.BranchSorter

class BranchSorterTest {

    @Test
    fun sorts_unsorted_branches_by_iteration_number_for_the_same_pairing_partner() {
        val sut = BranchSorter()

        val sortedBranches = sut.sort_branches(listOf(
                Branch("firstname_lastname_iteration_2_claus"),
                Branch("firstname_lastname_iteration_1_claus")),
                listOf("Claus"))

        assertThat(sortedBranches).containsExactly(
                Branch("firstname_lastname_iteration_1_claus"),
                Branch("firstname_lastname_iteration_2_claus"))
    }

    @Test
    fun sorts_unsorted_branches_by_pairing_partner_for_the_same_iteration_number() {
        val sut = BranchSorter()

        val sortedBranches = sut.sort_branches(listOf(
                Branch("firstname_lastname_iteration_1_claus"),
                Branch("firstname_lastname_iteration_1_berni")),
                listOf("Berni", "Claus"))

        assertThat(sortedBranches).containsExactly(
                Branch("firstname_lastname_iteration_1_berni"),
                Branch("firstname_lastname_iteration_1_claus"))
    }

}