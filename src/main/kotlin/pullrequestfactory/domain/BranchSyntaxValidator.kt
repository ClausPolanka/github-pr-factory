package pullrequestfactory.domain

class BranchSyntaxValidator(private val ui: UI) {

    fun validate(branch: Branch) {
        when {
            branch.parts().size != 5 -> throw InvalidBranchSyntax(errorMessageFor(branch))
            !Regex("[a-z]+_[a-z]+_iteration_[0-9]+_[a-z]+").matches(branch.name) -> {
                ui.show("[WARNING] Branch has incorrect syntax: $branch")
            }
        }
    }

    private fun errorMessageFor(branch: Branch) =
            "Following branch has an invalid name and therefore can't be processed: '${branch.name}'"

}
