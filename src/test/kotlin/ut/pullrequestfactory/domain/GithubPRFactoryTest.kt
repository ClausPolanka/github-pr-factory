package ut.pullrequestfactory.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.GithubRepo

internal class GithubPRFactoryTest {

    @Test
    fun create_pull_requests_for_candidate() {
        val githubRepo = mockk<GithubRepo>(relaxed = true)
        val sut = GithubPRFactory(githubRepo)
        val candidate = Candidate("Firstname", "Lastname")

        every {
            githubRepo.get_all_branches()
        } returns listOf(Branch("firstname_lastname_iteration_1_claus"))

        sut.create_pull_requests_for_candidate(candidate)

        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 1 / Session 1 Claus") }
    }

}
