package it.pullrequestfactory.io.clikt

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.io.clikt.CloseCommand
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.uis.ConsoleUI

class `Clikt CLOSE command integration tests` {

    private val candidateFirstName = "Firstname"
    private val candidateLastName = "Lastname"

    companion object {
        @ClassRule
        @JvmField
        val wireMockRule = WireMockRule()
    }

    @After
    fun tearDown() {
        reset()
    }

    @Test
    fun `CLOSE pull requests for given options`() {
        ensureHighEnoughRateLimit()

        val prs = (1..7).map {
            GetPullRequest(
                    number = it,
                    title = "$candidateFirstName $candidateLastName Iteration $it / Session $it pairingpartner-$it")
        }.toTypedArray()

        stubFor(get("/repos/ClausPolanka/wordcount/pulls?page=1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(toJson(prs))))

        `github-pr-factory CLOSE pull requests`(arrayOf(
                "-g", "any-github-token",
                "-fn", candidateFirstName,
                "-ln", candidateLastName))

        (1..7).forEach { verifyPatchRequestToCloseOpenPullRequestsFor(it) }
    }

    private fun `github-pr-factory CLOSE pull requests`(args: Array<String>) {
        CloseCommand(CommandArgs(
                baseUrl = "http://localhost:8080",
                repoPath = "/repos/ClausPolanka/wordcount",
                userPropertiesFile = "user.properties",
                ui = ConsoleUI()
        )).parse(args)
    }

    private fun verifyPatchRequestToCloseOpenPullRequestsFor(prNumber: Int) {
        verify(patchRequestedFor(urlMatching("/repos/ClausPolanka/wordcount/pulls/$prNumber"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .addCommonHeaders())
    }

    private fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
        return this.withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("token .*"))
                .withHeader("Content-Type", matching("application/json"))
    }
}
