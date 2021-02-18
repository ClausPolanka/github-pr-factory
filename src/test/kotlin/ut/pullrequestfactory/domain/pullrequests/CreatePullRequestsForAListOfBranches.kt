package ut.pullrequestfactory.domain.pullrequests

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.Branches
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.pullrequests.PullRequestLastNotFinishedMarker
import ut.pullrequestfactory.domain.TestBranchBuilder

class CreatePullRequestsForAListOfBranches {

    private val pairingPartner = PairingPartner.SHUBHI
    private val pairingPartner1 = PairingPartner.SHUBHI
    private val pairingPartner2 = PairingPartner.BERNI

    @Test
    fun `create pull request for one session within same iteration for same pairing partner`() {
        val branch = TestBranchBuilder()
            .withCandidate(Candidate("Firstname", "Lastname"))
            .withIteration(1)
            .withPairingPartner(pairingPartner)
            .build()
        val sut = createBranchesFor(listOf(branch))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner))

        assertThat(prs).containsExactly(
            PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestName()}",
                Branch("master"),
                branch
            )
        )
    }

    @Test
    fun `create pull requests for one session and two iterations for same pairing partner`() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner)
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner(pairingPartner)
            .build()
        val sut = createBranchesFor(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner))

        assertThat(prs).contains(
            PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestName()} [PR]",
                Branch("master"),
                branch1
            ),
            PullRequest(
                "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner.pullRequestName()}",
                branch1,
                branch2
            )
        )
    }

    @Test
    fun `create pull requests for two sessions within same iteration for different pairing partner`() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner1)
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner2)
            .build()
        val sut = createBranchesFor(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs).contains(
            PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestName()}",
                Branch("master"),
                branch1
            ),
            PullRequest(
                "Firstname Lastname Iteration 1 / Session 2 ${pairingPartner2.pullRequestName()}",
                branch1,
                branch2
            )
        )
    }

    @Test
    fun `create pull requests for two sessions for two iterations for different pairing partner`() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner("shubhi")
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner("berni")
            .build()
        val sut = createBranchesFor(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(listOf(PairingPartner.SHUBHI, PairingPartner.BERNI))

        assertThat(prs).contains(
            PullRequest("Firstname Lastname Iteration 1 / Session 1 Shubhi [PR]", Branch("master"), branch1),
            PullRequest("Firstname Lastname Iteration 2 / Session 2 Berni", branch1, branch2)
        )
    }

    @Test
    fun `create pull requests for two different sessions where in first session a new iteration is started`() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner1)
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner(pairingPartner1)
            .build()
        val branch3 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner(pairingPartner2)
            .build()
        val sut = createBranchesFor(listOf(branch1, branch2, branch3))

        val prs = sut.getPullRequestsFor(listOf(pairingPartner1, pairingPartner2))

        assertThat(prs).contains(
            PullRequest(
                "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestName()} [PR]",
                Branch("master"),
                branch1
            ),
            PullRequest(
                "Firstname Lastname Iteration 2 / Session 1 ${pairingPartner1.pullRequestName()}",
                branch1,
                branch2
            ),
            PullRequest(
                "Firstname Lastname Iteration 2 / Session 2 ${pairingPartner2.pullRequestName()}",
                branch2,
                branch3
            )
        )
    }

    @Test
    fun `support multiple sessions for same pairing partner`() {
        val branch1 = TestBranchBuilder().withBranchName("firstname_lastname_Iteration_1_tomas").build()
        val branch2 = TestBranchBuilder().withBranchName("firstname_lastname_Iteration_1_shubi").build()
        val branch3 = TestBranchBuilder().withBranchName("firstname_lastname_Iteration_1_tomasr").build()
        val sut = createBranchesFor(listOf(branch1, branch2, branch3))

        val prs = sut.getPullRequestsFor(listOf(PairingPartner.TOMAS, PairingPartner.SHUBHI, PairingPartner.TOMAS))

        assertThat(prs).contains(
            PullRequest("Firstname Lastname Iteration 1 / Session 1 Tomas", Branch("master"), branch1),
            PullRequest("Firstname Lastname Iteration 1 / Session 2 Shubhi", branch1, branch2),
            PullRequest("Firstname Lastname Iteration 1 / Session 3 Tomas", branch2, branch3)
        )
    }

    @Test
    fun `create pull requests where branches are in wrong order compared to given pairing partner`() {
        val branch1 = TestBranchBuilder().withBranchName("firstname_lastname_Iteration_1_berni").build()
        val branch2 = TestBranchBuilder().withBranchName("firstname_lastname_Iteration_1_claus").build()
        val sut = createBranchesFor(listOf(branch1, branch2))

        val prs = sut.getPullRequestsFor(listOf(PairingPartner.CLAUS, PairingPartner.BERNI))

        assertThat(prs).contains(
            PullRequest("Firstname Lastname Iteration 1 / Session 1 Claus", Branch("master"), branch2),
            PullRequest("Firstname Lastname Iteration 1 / Session 2 Berni", branch2, branch1)
        )
    }

    private fun createBranchesFor(branches: List<Branch>) = Branches(branches, PullRequestLastNotFinishedMarker())

}
