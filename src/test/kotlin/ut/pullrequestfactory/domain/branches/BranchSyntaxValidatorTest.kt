package ut.pullrequestfactory.domain.branches

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.uis.UI

class BranchSyntaxValidatorTest {

    @Test
    fun ui_shows_message_for_invalid_branch_names() {
        var message = ""
        val sut = BranchSyntaxValidator(object : UI {
            override fun show(msg: String) {
                message = msg
            }
        })
        listOf("_lastname_iteration_1_pairingpartner",
                "firstname_lastname_iter_1_pairingpartner",
                "firstname_lastname_iteration_x_pairingpartner",
                "firstname_lastname_iteration_1_").forEach {

            sut.validate(Branch(it))

            assertThat(message).withFailMessage("'$it' has correct syntax").contains("WARNING")
        }
    }

    @Test
    fun ui_shows_no_message_for_valid_branch_names() {
        var message = ""
        val sut = BranchSyntaxValidator(object : UI {
            override fun show(msg: String) {
                message = msg
            }
        })

        sut.validate(Branch("firstname_lastname_iteration_1_claus"))

        assertThat(message).isEmpty()
    }

}
