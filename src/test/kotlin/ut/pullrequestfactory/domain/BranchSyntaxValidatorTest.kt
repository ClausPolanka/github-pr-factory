package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.BranchSyntaxValidator
import pullrequestfactory.domain.UI

class BranchSyntaxValidatorTest {

    @Test
    fun ui_shows_message_for_invalid_branch_names() {
        val branchNames = listOf(
                "_lastname_iteration_1_claus",
                "firstname_iteration_1_claus",
                "firstname_lastname_iter_1_claus",
                "firstname_lastname_iteration_x_claus",
                "firstname_lastname_iteration_1_")
        branchNames.forEach {
            var expectedMsg = ""
            val sut = BranchSyntaxValidator(object : UI {
                override fun show(msg: String) {
                    expectedMsg = msg
                }
            })

            sut.validate(Branch(it))

            assertThat(expectedMsg).withFailMessage("'$it' has correct syntax").contains("WARNING")
        }
    }

    @Test
    fun ui_shows_no_message_for_valid_branch_names() {
        var expectedMsg = ""
        val sut = BranchSyntaxValidator(object : UI {
            override fun show(msg: String) {
                expectedMsg = msg
            }
        })

        sut.validate(Branch("firstname_lastname_iteration_1_claus"))

        assertThat(expectedMsg).isEmpty()
    }

}
