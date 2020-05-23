package ut.pullrequestfactory.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.Branch
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.GithubRepo

class GithubPRFactoryTest {

    @Test
    fun create_two_pull_requests_for_different_sessions_and_different_iterations() {
        val githubRepo = mockk<GithubRepo>(relaxed = true)
        val sut = GithubPRFactory(githubRepo)

        every {
            githubRepo.get_all_branches()
        } returns listOf(Branch("firstname_lastname_iteration_1_claus"), Branch("firstname_lastname_iteration_2_berni"))

        sut.create_pull_requests(Candidate("Firstname", "Lastname"))

        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 1 / Session 1 Claus") }
        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 2 / Session 2 Berni") }
    }
}
