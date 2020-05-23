package it.pullrequestfactory.io

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.domain.GithubWriteRepo
import pullrequestfactory.domain.PullRequest
import pullrequestfactory.io.GithubFileReadRepo

class GithubPRFactoryTest {

    @Test
    fun create_pull_requests_from_json_files() {
        val pullRequests = mutableListOf<PullRequest>()
        val sut = GithubPRFactory(GithubFileReadRepo(), object : GithubWriteRepo {
            override fun create_pull_request(pullRequest: PullRequest) {
                pullRequests.add(pullRequest)
            }
        })
        sut.create_pull_requests(Candidate("Radek", "Leifer"), listOf("Claus", "Berni", "Dominik", "Christian", "Shubi", "Markus", "Mihai"))
        assertThat(pullRequests).hasSize(8)
    }
}
