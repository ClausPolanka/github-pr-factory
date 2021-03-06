package ut.pullrequestfactory.domain.branches

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.BranchSyntaxValidator
import pullrequestfactory.domain.uis.UI

class BranchSyntaxValidatorTest {

    @Test
    fun `ui shows message for invalid branch names`() {
        var message = ""
        val sut = BranchSyntaxValidator(object : UI {
            override fun show(msg: String) {
                message = msg
            }

            override fun getUserInput(msg: String): String {
                ignore()
            }
        })
        listOf(
            "_lastname_iteration_1_pairingpartner",
            "firstname_lastname_iter_1_pairingpartner",
            "firstname_lastname_iteration_x_pairingpartner",
            "firstname_lastname_iteration_1_"
        ).forEach {
            sut.validate(Branch(it))
            assertThat(message)
                .withFailMessage("'$it' has correct syntax")
                .contains("WARNING")
        }
    }

    @Test
    fun `ui shows message for invalid branch names containing an unknown pairing partner`() {
        var message = ""
        val sut = BranchSyntaxValidator(object : UI {
            override fun show(msg: String) {
                message = msg
            }

            override fun getUserInput(msg: String): String {
                ignore()
            }
        })
        listOf(
            "firstname_lastname_iteration_1_marci",
            "firstname_lastname_iteration_1_martin"
        ).forEach {
            sut.validate(Branch(it))
            assertThat(message)
                .withFailMessage("'$it' has known pairing partner")
                .contains("WARNING")
        }
    }

    @Test
    fun `ui shows no message for valid branch names`() {
        var message = ""
        val sut = BranchSyntaxValidator(object : UI {
            override fun show(msg: String) {
                message = msg
            }

            override fun getUserInput(msg: String): String {
                ignore()
            }
        })

        sut.validate(Branch("firstname_lastname_iteration_1_claus"))

        assertThat(message).isEmpty()
    }

    private fun ignore(): Nothing {
        throw NotImplementedError("Ooperation is not required")
    }

}
