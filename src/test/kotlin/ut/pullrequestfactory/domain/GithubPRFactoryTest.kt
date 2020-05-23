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
    fun create_one_pull_request() {
        val githubRepo = mockk<GithubRepo>(relaxed = true)
        val sut = GithubPRFactory(githubRepo)
        val candidate = Candidate("Firstname", "Lastname")

        every {
            githubRepo.get_all_branches()
        } returns listOf(Branch("firstname_lastname_iteration_1_claus"))

        sut.create_pull_requests_for_candidate(candidate)

        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 1 / Session 1 Claus") }
    }

    @Test
    fun create_two_pull_requests_for_same_pairing_partner_but_different_iterations() {
        val githubRepo = mockk<GithubRepo>(relaxed = true)
        val sut = GithubPRFactory(githubRepo)
        val candidate = Candidate("Firstname", "Lastname")

        every {
            githubRepo.get_all_branches()
        } returns listOf(Branch("firstname_lastname_iteration_1_claus"), Branch("firstname_lastname_iteration_2_claus"))

        sut.create_pull_requests_for_candidate(candidate)

        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 1 / Session 1 Claus") }
        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 2 / Session 1 Claus") }
    }

    @Test
    fun create_two_pull_requests_for_same_iteration_but_different_sessions_and_pairing_partners() {
        val githubRepo = mockk<GithubRepo>(relaxed = true)
        val sut = GithubPRFactory(githubRepo)
        val candidate = Candidate("Firstname", "Lastname")

        every {
            githubRepo.get_all_branches()
        } returns listOf(Branch("firstname_lastname_iteration_1_claus"), Branch("firstname_lastname_iteration_1_berni"))

        sut.create_pull_requests_for_candidate(candidate)

        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 1 / Session 1 Claus") }
        verify { githubRepo.create_pull_request("Firstname Lastname Iteration 1 / Session 2 Berni") }
    }

}
