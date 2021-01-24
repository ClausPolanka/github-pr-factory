package it.pullrequestfactory.io.clikt

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.PairingPartner.*
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.clikt.OpenCommand
import pullrequestfactory.io.programs.impl.Rate
import pullrequestfactory.io.programs.impl.RateLimit
import pullrequestfactory.io.uis.ConsoleUI
import java.time.LocalDateTime
import java.time.ZoneId

class `Clikt OPEN command integration tests` {

    private val candidateFirstName = "firstname"
    private val candidateLastName = "lastname"

    private val anyArgs = arrayOf(
            "-g", "any-github-token",
            "-fn", "firstname",
            "-ln", "lastname",
            "-pp1", "markus",
            "-pp2", "berni",
            "-pp3", "lukas",
            "-pp4", "jakub",
            "-pp5", "peter",
            "-pp6", "christian",
            "-pp7", "vaclav")

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
    fun `OPEN pull requests for given options`() {
        ensureHighEnoughRateLimit()

        val branches = branchesFor(
                listOf(MARKUS, BERNI, LUKAS, JAKUB, PETER, CHRISTIAN, VACLAV),
                candidateFirstName,
                candidateLastName)

        stubFor(get("/repos/ClausPolanka/wordcount/branches?page=1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(toJson(branches))))

        `github-pr-factory OPEN pull requests`(arrayOf(
                "-g", "any-github-token",
                "-fn", candidateFirstName,
                "-ln", candidateLastName,
                "-pp1", MARKUS.pull_request_name(),
                "-pp2", BERNI.pull_request_name(),
                "-pp3", LUKAS.pull_request_name(),
                "-pp4", JAKUB.pull_request_name(),
                "-pp5", PETER.pull_request_name(),
                "-pp6", CHRISTIAN.pull_request_name(),
                "-pp7", VACLAV.pull_request_name()))

        verify(PullRequest("Firstname Lastname Iteration 1 / Session 1 Markus [PR]",
                Branch("master"),
                Branch("firstname_lastname_iteration_1_markus")))
        verify(PullRequest("Firstname Lastname Iteration 2 / Session 2 Berni [PR]",
                Branch("firstname_lastname_iteration_1_markus"),
                Branch("firstname_lastname_iteration_2_berni")))
        verify(PullRequest("Firstname Lastname Iteration 3 / Session 3 Lukas [PR]",
                Branch("firstname_lastname_iteration_2_berni"),
                Branch("firstname_lastname_iteration_3_lukas")))
        verify(PullRequest("Firstname Lastname Iteration 4 / Session 4 Jakub [PR]",
                Branch("firstname_lastname_iteration_3_lukas"),
                Branch("firstname_lastname_iteration_4_jakub")))
        verify(PullRequest("Firstname Lastname Iteration 5 / Session 5 Peter [PR]",
                Branch("firstname_lastname_iteration_4_jakub"),
                Branch("firstname_lastname_iteration_5_peter")))
        verify(PullRequest("Firstname Lastname Iteration 6 / Session 6 Christian [PR]",
                Branch("firstname_lastname_iteration_5_peter"),
                Branch("firstname_lastname_iteration_6_christian")))
        verify(PullRequest("Firstname Lastname Iteration 7 / Session 7 Vaclav",
                Branch("firstname_lastname_iteration_6_christian"),
                Branch("firstname_lastname_iteration_7_vaclav")))
    }

    @Test
    fun `OPEN pull requests where last session is considered to be finished`() {
        ensureHighEnoughRateLimit()

        val branches = branchesFor(
                listOf(MARKUS, BERNI, LUKAS, JAKUB, PETER, CHRISTIAN, VACLAV),
                candidateFirstName,
                candidateLastName)

        stubFor(get("/repos/ClausPolanka/wordcount/branches?page=1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(toJson(branches))))

        `github-pr-factory OPEN pull requests`(arrayOf(
                "--last-finished",
                "-g", "any-github-token",
                "-fn", candidateFirstName,
                "-ln", candidateLastName,
                "-pp1", MARKUS.pull_request_name(),
                "-pp2", BERNI.pull_request_name(),
                "-pp3", LUKAS.pull_request_name(),
                "-pp4", JAKUB.pull_request_name(),
                "-pp5", PETER.pull_request_name(),
                "-pp6", CHRISTIAN.pull_request_name(),
                "-pp7", VACLAV.pull_request_name()))

        verify(PullRequest("Firstname Lastname Iteration 1 / Session 1 Markus [PR]",
                Branch("master"),
                Branch("firstname_lastname_iteration_1_markus")))
        verify(PullRequest("Firstname Lastname Iteration 2 / Session 2 Berni [PR]",
                Branch("firstname_lastname_iteration_1_markus"),
                Branch("firstname_lastname_iteration_2_berni")))
        verify(PullRequest("Firstname Lastname Iteration 3 / Session 3 Lukas [PR]",
                Branch("firstname_lastname_iteration_2_berni"),
                Branch("firstname_lastname_iteration_3_lukas")))
        verify(PullRequest("Firstname Lastname Iteration 4 / Session 4 Jakub [PR]",
                Branch("firstname_lastname_iteration_3_lukas"),
                Branch("firstname_lastname_iteration_4_jakub")))
        verify(PullRequest("Firstname Lastname Iteration 5 / Session 5 Peter [PR]",
                Branch("firstname_lastname_iteration_4_jakub"),
                Branch("firstname_lastname_iteration_5_peter")))
        verify(PullRequest("Firstname Lastname Iteration 6 / Session 6 Christian [PR]",
                Branch("firstname_lastname_iteration_5_peter"),
                Branch("firstname_lastname_iteration_6_christian")))
        verify(PullRequest("Firstname Lastname Iteration 7 / Session 7 Vaclav [PR]",
                Branch("firstname_lastname_iteration_6_christian"),
                Branch("firstname_lastname_iteration_7_vaclav")))
    }

    @Test
    fun `OPEN pull requests where rate limit is not sufficient to fullfil the request`() {
        stubFor(get("/rate_limit").willReturn(aResponse()
                .withStatus(403)
                .withBody(Klaxon().toJsonString(RateLimit(Rate(
                        limit = 50,
                        used = 50,
                        remaining = 0,
                        reset = fromNowInOneHour()))))))

        val actualOutputCapture = mutableListOf<String>()

        OpenCommand(argsWith(fakeUI(actualOutputCapture))).parse(anyArgs)

        assertThat(actualOutputCapture.any { it.contains("remaining=0") }).isTrue()
    }

    private fun branchesFor(pairingPartner: List<PairingPartner>, fn: String, ln: String): Array<Branch> =
            pairingPartner
                    .map { it.name.toLowerCase() }
                    .mapIndexed { i, pp -> Branch("${fn}_${ln}_iteration_${i.inc()}_$pp") }
                    .toTypedArray()

    private fun `github-pr-factory OPEN pull requests`(args: Array<String>) {
        OpenCommand(CommandArgs(
                baseUrl = "http://localhost:8080",
                repoPath = "/repos/ClausPolanka/wordcount",
                userPropertiesFile = "user.properties",
                ui = ConsoleUI()
        )).parse(args)
    }

    private fun verify(pr: PullRequest) {
        verify(postRequestedFor(urlMatching("/repos/ClausPolanka/wordcount/pulls"))
                .withRequestBody(matching(Regex.escape(Klaxon().toJsonString(pr))))
                .addCommonHeaders())
    }

    private fun fromNowInOneHour() =
            LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()

    private fun argsWith(ui: UI) =
            CommandArgs(
                    baseUrl = "http://localhost:8080",
                    repoPath = "/repos/ClausPolanka/wordcount",
                    userPropertiesFile = "user.properties",
                    ui = ui)

    private fun fakeUI(outputCapture: MutableList<String>) =
            object : UI {
                override fun show(msg: String) {
                    outputCapture.add(msg)
                }

                override fun get_user_input(msg: String): String {
                    TODO("Ignore")
                }
            }
}
