package pullrequestfactory.domain

class BranchSyntaxValidator(private val ui: UI) {
    fun validate(branch: Branch) {
        when {
            !Regex("[a-z]+_[a-z]+_iteration_[0-9]+_[a-z]+").matches(branch.name) -> {
                ui.show("[WARNING] Branch has incorrect syntax: $branch")
            }
        }
    }

}
