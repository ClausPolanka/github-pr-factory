package pullrequestfactory.domain.branches

import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.uis.UI

class BranchSyntaxValidator(private val ui: UI) {

    private val branchSyntaxRegex = Regex("[a-z]+_[a-z]+_[iI]teration_[0-9]+_[a-z]+")

    fun validate(branch: Branch) {
        when {
            branch.parts().size != 5 -> throw InvalidBranchSyntax(errorMessageFor(branch))

            !branchSyntaxRegex.matches(branch.name) -> {
                ui.show("[WARNING] Branch has incorrect syntax: $branch")
            }

            PairingPartner.from(branch.pairingPartner()) == null -> {
                ui.show("[WARNING] Branch contains unknown pairing partner: $branch")
            }
        }

    }

    private fun errorMessageFor(branch: Branch) =
        "Following branch has an invalid name and therefore can't be processed: '${branch.name}'"

    class InvalidBranchSyntax(msg: String) : RuntimeException(msg)

}
