package it.pullrequestfactory.io.clikt

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.pullrequests.GetPullRequest
import pullrequestfactory.domain.uis.QuietUI
import pullrequestfactory.io.clikt.CloseCommand
import pullrequestfactory.io.programs.impl.InstantSerializer

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
                        .withBody(Json { serializersModule = SerializersModule { contextual(InstantSerializer) } }.encodeToString(arrayOf(pr1, pr2)))
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
        CloseCommand(cmdArgsFor(repoPath, QuietUI())).parse(args)
    }

    private fun verifyPatchRequestToCloseOpenPullRequestsFor(prNumber: Int) {
        verify(
            patchRequestedFor(urlMatching("$repoPath/pulls/$prNumber"))
                .withRequestBody(equalToJson("""{"state":"closed"}"""))
                .addCommonHeaders()
        )
    }

}
