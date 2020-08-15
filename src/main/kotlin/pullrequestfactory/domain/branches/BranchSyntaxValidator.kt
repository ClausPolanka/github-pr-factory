package pullrequestfactory.domain.branches

import pullrequestfactory.domain.uis.UI

class BranchSyntaxValidator(private val ui: UI) {

    private val branchSyntaxRegex = Regex("[a-z]+_[a-z]+_[iI]teration_[0-9]+_[a-z]+")

    fun validate(branch: Branch) {
        when {
            branch.parts().size != 5 -> throw InvalidBranchSyntax(error_message_for(branch))

            !branchSyntaxRegex.matches(branch.name) -> {
                ui.show("[WARNING] Branch has incorrect syntax: $branch")
            }
        }
    }

    private fun error_message_for(branch: Branch) =
            "Following branch has an invalid name and therefore can't be processed: '${branch.name}'"

    class InvalidBranchSyntax(msg: String) : RuntimeException(msg)

}
