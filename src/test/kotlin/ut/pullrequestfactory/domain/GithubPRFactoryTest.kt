package ut.pullrequestfactory.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.*

class GithubPRFactoryTest {

    @Test
    fun create_two_pull_requests_for_different_sessions_and_different_iterations() {
        val githubReadRepo = mockk<GithubReadRepo>(relaxed = true)
        val githubWriteRepo = mockk<GithubWriteRepo>(relaxed = true)
        val sut = GithubPRFactory(githubReadRepo, githubWriteRepo)

        every {
            githubReadRepo.get_all_branches()
        } returns listOf(Branch("firstname_lastname_iteration_1_claus"), Branch("firstname_lastname_iteration_2_berni"))

        sut.create_pull_requests(Candidate("Firstname", "Lastname"))

        verify { githubWriteRepo.create_pull_request(PullRequest("Firstname Lastname Iteration 1 / Session 1 Claus", "master", "firstname_lastname_iteration_1_claus")) }
        verify { githubWriteRepo.create_pull_request(PullRequest("Firstname Lastname Iteration 2 / Session 2 Berni", "firstname_lastname_iteration_1_claus", "firstname_lastname_iteration_2_berni")) }
    }
}
