package it.pullrequestfactory.io.clikt

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.uis.QuietUI
import pullrequestfactory.io.clikt.CloseCommand
import pullrequestfactory.io.clikt.CommandArgs

class CloseCommandIT {

    private val repoPath = "/repos/ClausPolanka/wordcount"
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

        val pr1 = prFor(candidateFirstName, candidateLastName, prNr = 1)
        val pr2 = prFor(candidateFirstName, candidateLastName, prNr = 2)

        stubFor(
            get(urlPathMatching("$repoPath/pulls(\\?page\\=1)?"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(toJson(arrayOf(pr1, pr2)))
                )
        )

        `github-pr-factory CLOSE pull requests`(
            arrayOf(
                "-g", "any-github-token",
                "-fn", candidateFirstName,
                "-ln", candidateLastName
            )
        )

        verifyPatchRequestToCloseOpenPullRequestsFor(pr1.number)
        verifyPatchRequestToCloseOpenPullRequestsFor(pr2.number)
    }

    private fun prFor(firstName: String, lastName: String, prNr: Int) =
        GetPullRequest(
            number = prNr,
            title = "$firstName $lastName Iteration $prNr / Session $prNr pairingpartner-$prNr"
        )

    private fun `github-pr-factory CLOSE pull requests`(args: Array<String>) {
        CloseCommand(
            CommandArgs(
                baseUrl = "http://localhost:8080",
                repoPath = repoPath,
                userPropertiesFile = "user.properties",
                ui = QuietUI()
            )
        ).parse(args)
    }

    private fun verifyPatchRequestToCloseOpenPullRequestsFor(prNumber: Int) {
        verify(
            patchRequestedFor(urlMatching("$repoPath/pulls/$prNumber"))
                .withRequestBody(matching(Regex.escape("""{"state" : "closed"}""")))
                .addCommonHeaders()
        )
    }

}
