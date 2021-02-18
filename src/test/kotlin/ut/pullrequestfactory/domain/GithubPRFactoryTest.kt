package ut.pullrequestfactory.domain

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.branches.GithubBranchesRepo
import pullrequestfactory.domain.pullrequests.*
import pullrequestfactory.domain.uis.QuietUI

class GithubPRFactoryTest {

    private val invalidBranchName = "firstname_lastname_claus"
    private val candidate = Candidate("Firstname", "Lastname")
    private val pairingPartner = PairingPartner.SHUBHI
    private val pairingPartner1 = PairingPartner.SHUBHI
    private val pairingPartner2 = PairingPartner.BERNI

    @Test
    fun `opens two pull requests for different iterations and pairing partner`() {
        val candidate = Candidate("Firstname", "Lastname")
        val branch1 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner1)
            .build()
        val branch2 = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(2)
            .withPairingPartner(pairingPartner2)
            .build()
        val (pullRequests, sut) = createGithubPrFactory(listOf(branch1, branch2))

        sut.openPullRequests(candidate, listOf(pairingPartner1, pairingPartner2))

        assertThat(pullRequests).containsExactly(
            PullRequest(
                title = "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner1.pullRequestName()} [PR]",
                _base = Branch("master"),
                _head = branch1
            ),
            PullRequest(
                title = "Firstname Lastname Iteration 2 / Session 2 ${pairingPartner2.pullRequestName()}",
                _base = branch1,
                _head = branch2
            )
        )
    }

    @Test
    fun `opens pull request and ignores if candidates first name is capitalized`() {
        val candidate = Candidate("Firstname", "lastname")
        val branch = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner)
            .build()
        val (pullRequests, sut) = createGithubPrFactory(listOf(branch))

        sut.openPullRequests(candidate, listOf(pairingPartner))

        assertThat(pullRequests).containsExactly(
            PullRequest(
                title = "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestName()}",
                _base = Branch("master"),
                _head = branch
            )
        )
    }

    @Test
    fun `opens pull request and ignores if candidates last name is capitalized`() {
        val candidate = Candidate("firstname", "Lastname")
        val branch = TestBranchBuilder()
            .withCandidate(candidate)
            .withIteration(1)
            .withPairingPartner(pairingPartner)
            .build()
        val (pullRequests, sut) = createGithubPrFactory(listOf(branch))

        sut.openPullRequests(candidate, listOf(pairingPartner))

        assertThat(pullRequests).containsExactly(
            PullRequest(
                title = "Firstname Lastname Iteration 1 / Session 1 ${pairingPartner.pullRequestName()}",
                _base = Branch("master"),
                _head = branch
            )
        )
    }

    @Test
    fun `opens no pull requests for candidate when no branch exists containing candidates first name`() {
        val (pullRequests, sut) = createGithubPrFactory(
            listOf(
                TestBranchBuilder()
                    .withCandidate(Candidate("Firstname", "Lastname"))
                    .withIteration(1)
                    .withPairingPartner(pairingPartner)
                    .build()
            )
        )

        sut.openPullRequests(Candidate("firstname2", "Lastname"), listOf(pairingPartner))

        assertThat(pullRequests).isEmpty()
    }

    @Test
    fun `opens no pull requests for candidate when no branch exists containing candidates last name`() {
        val (pullRequests, sut) = createGithubPrFactory(
            listOf(
                TestBranchBuilder()
                    .withCandidate(Candidate("Firstname", "Lastname"))
                    .withIteration(1)
                    .withPairingPartner(pairingPartner1)
                    .build()
            )
        )

        sut.openPullRequests(Candidate("Firstname", "lastname2"), listOf(pairingPartner))

        assertThat(pullRequests).isEmpty()
    }

    @Test
    fun `branch can not be processed if branch name has invalid name`() {
        val sut = createGithubPrFactoryFor(invalidBranchName)

        assertThatThrownBy { sut.openPullRequests(candidate, listOf(pairingPartner)) }
            .hasMessageContaining("invalid name")
            .hasMessageContaining(invalidBranchName)
    }

    @Test
    fun `close pull requests for two candidates with same first name`() {
        val (pullRequestNumbersToBeClosed, sut) = createGithubPrFactoryFor(
            listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}"),
                GetPullRequest(2, "Firstname1 Lastname2 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}")
            )
        )

        sut.closePullRequestsFor(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
            .describedAs("Expected pull request numbers to be closed")
            .containsExactly(1)
    }

    @Test
    fun `close pull requests for two candidates with same last name`() {
        val (pullRequestNumbersToBeClosed, sut) = createGithubPrFactoryFor(
            listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}"),
                GetPullRequest(2, "Firstname2 Lastname1 Iteration 1 / Session 1 ${pairingPartner.name.toLowerCase()}")
            )
        )

        sut.closePullRequestsFor(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
            .describedAs("Expected pull request numbers to be closed")
            .containsExactly(1)
    }

    @Test
    fun `close pull requests for one candidate`() {
        val (pullRequestNumbersToBeClosed, sut) = createGithubPrFactoryFor(
            listOf(
                GetPullRequest(1, "Firstname1 Lastname1 Iteration 1 / Session 1 ${pairingPartner1.name.toLowerCase()}"),
                GetPullRequest(2, "Firstname1 Lastname1 Iteration 1 / Session 2 ${pairingPartner2.name.toLowerCase()}")
            )
        )

        sut.closePullRequestsFor(Candidate("firstname1", "lastname1"))

        assertThat(pullRequestNumbersToBeClosed)
            .describedAs("Expected pull request numbers to be closed")
            .containsExactly(1, 2)
    }

    private fun createGithubPrFactory(branches: List<Branch>): Pair<MutableList<PullRequest>, GithubPRFactory> {
        val pullRequests = mutableListOf<PullRequest>()
        val githubBranchesRepo = githubBranchesRepo(branches)
        val sut = GithubPRFactory(
            QuietUI(),
            githubBranchesRepo,
            githubPullRequestsRepo(github_write_repo(pullRequests)),
            PullRequestLastNotFinishedMarker()
        )
        return Pair(pullRequests, sut)
    }

    private fun createGithubPrFactoryFor(pullRequests: List<GetPullRequest>): Pair<MutableList<Int>, GithubPRFactory> {
        val pullRequestNumbersToBeClosed = mutableListOf<Int>()
        val sut = GithubPRFactory(
            QuietUI(),
            githubBranchesRepo(emptyList()),
            githubPullRequestsRepo(
                github_write_repo(mutableListOf(), pullRequestNumbersToBeClosed),
                pullRequests.toMutableList()
            ),
            PullRequestLastNotFinishedMarker()
        )
        return Pair(pullRequestNumbersToBeClosed, sut)
    }

    private fun createGithubPrFactoryFor(branchName: String) =
        GithubPRFactory(
            QuietUI(),
            githubBranchesRepo(listOf(Branch(branchName))),
            githubPullRequestsRepo(noopGithubWriteRepo()),
            PullRequestLastNotFinishedMarker()
        )

    private fun githubBranchesRepo(branches: List<Branch>): GithubBranchesRepo {
        return object : GithubBranchesRepo {
            override fun getBranches(): List<Branch> {
                return branches
            }
        }
    }

    private fun githubPullRequestsRepo(
        writeRepo: GithubPullRequestsWriteRepo,
        expectedPrs: MutableList<GetPullRequest> = mutableListOf()
    ): GithubPullRequestsRepo {
        return object : GithubPullRequestsRepo {
            override fun getPullRequests(): List<GetPullRequest> {
                return expectedPrs
            }

            override fun openPullRequest(pullRequest: PullRequest) {
                writeRepo.openPullRequest(pullRequest)
            }

            override fun closePullRequest(number: Int) {
                writeRepo.closePullRequest(number)
            }
        }
    }

    private fun github_write_repo(
        expectedPrs: MutableList<PullRequest>,
        expectedPullRequestNumbersToBeClosed: MutableList<Int> = mutableListOf()
    ): GithubPullRequestsWriteRepo {
        return object : GithubPullRequestsWriteRepo {
            override fun openPullRequest(pullRequest: PullRequest) {
                expectedPrs.add(pullRequest)
            }

            override fun closePullRequest(number: Int) {
                expectedPullRequestNumbersToBeClosed.add(number)
            }
        }
    }

    private fun noopGithubWriteRepo() = object : GithubPullRequestsWriteRepo {
        override fun openPullRequest(pullRequest: PullRequest) {
            // can be ignored in this test
        }

        override fun closePullRequest(number: Int) {
            // can be ignored in this test
        }
    }

}
