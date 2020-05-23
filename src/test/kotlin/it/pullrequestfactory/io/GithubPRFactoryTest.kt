package it.pullrequestfactory.io

import org.junit.jupiter.api.Test
import pullrequestfactory.domain.Candidate
import pullrequestfactory.domain.GithubPRFactory
import pullrequestfactory.io.GithubFileRepo

class GithubPRFactoryTest {

    @Test
    fun foo() {
        val sut = GithubPRFactory(GithubFileRepo())
        sut.create_pull_requests(Candidate("Radek", "Leifer"))
    }
}
