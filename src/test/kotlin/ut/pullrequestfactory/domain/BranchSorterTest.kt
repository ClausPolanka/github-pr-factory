package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.BranchSorter

internal class BranchSorterTest {

    @Test
    fun sorts_unsorted_branches_for_the_same_pairing_partner() {
        val sut = BranchSorter()

        val sortedBranches = sut.sort_branches(
                listOf(Branch("firstname_lastname_iteration_2_claus"), Branch("firstname_lastname_iteration_1_claus")),
                listOf("Claus"))

        assertThat(sortedBranches).containsExactly(
                Branch("firstname_lastname_iteration_1_claus"),
                Branch("firstname_lastname_iteration_2_claus"))
    }

}
