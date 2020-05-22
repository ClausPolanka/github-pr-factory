package pullrequestfactory.io

import com.beust.klaxon.Klaxon
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.Candidate
import java.net.URL

class GithubHttpRepo(val repoName: String) {
    fun find_branches_for_candidate(candidate: Candidate): List<Branch> {
        val jsonBranches = URL("https://api.github.com/repos/ClausPolanka/$repoName/branches").readText()
        val branches = Klaxon().parseArray<Branch>(jsonBranches)
        return emptyList()
    }

}
