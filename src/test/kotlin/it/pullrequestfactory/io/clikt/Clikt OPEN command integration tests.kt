package it.pullrequestfactory.io.clikt

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.clikt.OpenCommand
import pullrequestfactory.io.uis.ConsoleUI
import java.time.LocalDateTime
import java.time.ZoneId

class `Clikt OPEN command integration tests` {

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
        stubRateLimit()

        val br1 = Branch("firstname_lastname_iteration_1_markus")
        val br2 = Branch("firstname_lastname_iteration_2_berni")
        val br3 = Branch("firstname_lastname_iteration_3_lukas")
        val br4 = Branch("firstname_lastname_iteration_4_jakub")
        val br5 = Branch("firstname_lastname_iteration_5_peter")
        val br6 = Branch("firstname_lastname_iteration_6_christian")
        val br7 = Branch("firstname_lastname_iteration_7_vaclav")

        stubFor(get("/repos/ClausPolanka/wordcount/branches?page=1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(toJson(arrayOf(br1, br2, br3, br4, br5, br6, br7)))))

        `github-pr-factory OPEN pull requests`(arrayOf(
                "-g",
                "any-github-token",
                "-fn", "firstname", "-ln", "lastname",
                "-pp1", "markus",
                "-pp2", "berni",
                "-pp3", "lukas",
                "-pp4", "jakub",
                "-pp5", "peter",
                "-pp6", "christian",
                "-pp7", "vaclav",
        ))

        verify(PullRequest("Firstname Lastname Iteration 1 / Session 1 Markus [PR]", Branch("master"), Branch("firstname_lastname_iteration_1_markus")))
        verify(PullRequest("Firstname Lastname Iteration 2 / Session 2 Berni [PR]", Branch("firstname_lastname_iteration_1_markus"), Branch("firstname_lastname_iteration_2_berni")))
        verify(PullRequest("Firstname Lastname Iteration 3 / Session 3 Lukas [PR]", Branch("firstname_lastname_iteration_2_berni"), Branch("firstname_lastname_iteration_3_lukas")))
        verify(PullRequest("Firstname Lastname Iteration 4 / Session 4 Jakub [PR]", Branch("firstname_lastname_iteration_3_lukas"), Branch("firstname_lastname_iteration_4_jakub")))
        verify(PullRequest("Firstname Lastname Iteration 5 / Session 5 Peter [PR]", Branch("firstname_lastname_iteration_4_jakub"), Branch("firstname_lastname_iteration_5_peter")))
        verify(PullRequest("Firstname Lastname Iteration 6 / Session 6 Christian [PR]", Branch("firstname_lastname_iteration_5_peter"), Branch("firstname_lastname_iteration_6_christian")))
        verify(PullRequest("Firstname Lastname Iteration 7 / Session 7 Vaclav", Branch("firstname_lastname_iteration_6_christian"), Branch("firstname_lastname_iteration_7_vaclav")))
    }

    @Test
    fun `OPEN pull requests where last session is considered to be finished`() {
        stubRateLimit()

        val br1 = Branch("firstname_lastname_iteration_1_markus")
        val br2 = Branch("firstname_lastname_iteration_2_berni")
        val br3 = Branch("firstname_lastname_iteration_3_lukas")
        val br4 = Branch("firstname_lastname_iteration_4_jakub")
        val br5 = Branch("firstname_lastname_iteration_5_peter")
        val br6 = Branch("firstname_lastname_iteration_6_christian")
        val br7 = Branch("firstname_lastname_iteration_7_vaclav")

        stubFor(get("/repos/ClausPolanka/wordcount/branches?page=1")
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody(toJson(arrayOf(br1, br2, br3, br4, br5, br6, br7)))))

        `github-pr-factory OPEN pull requests`(arrayOf(
                "--last-finished",
                "-g",
                "any-github-token",
                "-fn", "firstname", "-ln", "lastname",
                "-pp1", "markus",
                "-pp2", "berni",
                "-pp3", "lukas",
                "-pp4", "jakub",
                "-pp5", "peter",
                "-pp6", "christian",
                "-pp7", "vaclav",
        ))
    }

    @Test
    fun `OPEN pull requests where rate limit is not sufficient to fullfil the request`() {
        stubFor(get("/rate_limit").willReturn(aResponse()
                .withStatus(403)
                .withBody("{ \n  \"rate\":  {\n    \"limit\": 50,\n    \"used\": 50,\n    \"remaining\": ${0},\n    \"reset\": ${LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()}\n  }\n}")))

        val output = mutableListOf<String>()
        OpenCommand(CommandArgs(
                baseUrl = "http://localhost:8080",
                repoPath = "/repos/ClausPolanka/wordcount",
                userPropertiesFile = "user.properties",
                ui = object : UI {
                    override fun show(msg: String) {
                        output.add(msg)
                    }

                    override fun get_user_input(msg: String): String {
                        TODO("not implemented")
                    }
                }
        )).parse(anyArgs)

        assertThat(output.any { it.contains("remaining=0") }).isTrue()
    }

    private fun stubRateLimit(remaining: Int = 5000, resetInMillisSinceEpoch: Long = 1608411669) {
        stubFor(get("/rate_limit").willReturn(aResponse()
                .withStatus(200)
                .withBody("{ \n  \"rate\":  {\n    \"limit\": 5000,\n    \"used\": 0,\n    \"remaining\": $remaining,\n    \"reset\": $resetInMillisSinceEpoch\n  }\n}")))
    }

    private fun toJson(branches: Array<Branch>) = Klaxon().toJsonString(branches)

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

    private fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
        return this.withHeader("Accept", matching("application/json"))
                .withHeader("Authorization", matching("token .*"))
                .withHeader("Content-Type", matching("application/json"))
    }

    fun execute(fn: (args: Array<String>) -> Unit, args: Array<String>, `and expect`: String) {
        Assertions.assertThatThrownBy { fn(args) }
                .hasMessage(`and expect`)
    }

}
