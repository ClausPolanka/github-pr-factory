package it.pullrequestfactory.io.clikt

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.After
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Test
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.clikt.OpenCommand

class `Clikt OPEN command integration tests` {

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
    @Ignore("Needs WireMock")
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

        val `open command output` = "foo"
        execute(::`github-pr-factory with`,
                arrayOf(
                        "-fn", "firstname", "-ln", "lastname",
                        "-pp1", "markus",
                        "-pp2", "berni",
                        "-pp3", "lukas",
                        "-pp4", "jakub",
                        "-pp5", "peter",
                        "-pp6", "christian",
                        "-pp7", "vaclav",
                ), `and expect` = `open command output`)
    }

    private fun stubRateLimit(remaining: Int = 5000, resetInMillisSinceEpoch: Long = 1608411669) {
        stubFor(get("/rate_limit").willReturn(aResponse()
                .withStatus(200)
                .withBody("{ \n  \"rate\":  {\n    \"limit\": 5000,\n    \"used\": 0,\n    \"remaining\": $remaining,\n    \"reset\": $resetInMillisSinceEpoch\n  }\n}")))
    }

    private fun toJson(branches: Array<Branch>) = Klaxon().toJsonString(branches)

    private fun `github-pr-factory with`(args: Array<String>) {
        OpenCommand(CommandArgs(
                baseUrl = "http://localhost:8080",
                repoPath = "/repos/ClausPolanka/wordcount",
                userPropertiesFile = "user.properties"
        )).parse(args)
    }

    fun execute(fn: (args: Array<String>) -> Unit, args: Array<String>, `and expect`: String) {
        assertThatThrownBy { fn(args) }
                .hasMessage(`and expect`)
    }
}
