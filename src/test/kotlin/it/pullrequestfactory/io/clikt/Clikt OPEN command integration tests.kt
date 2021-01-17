package it.pullrequestfactory.io.clikt

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.After
import org.junit.ClassRule
import org.junit.Ignore
import org.junit.Test
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

        val `open command output` = "foo"
        execute(::`github-pr-factory with`,
                arrayOf("-fn", "firstname", "-ln", "lastname",
                        "-pp1", "markus",
                        "-pp2", "berni",
                        "-pp3", "lukas",
                        "-pp4", "jakub",
                        "-pp5", "peter",
                        "-pp6", "christian",
                        "-pp7", "vaclav",
                ), `and expect` = `open command output`)
    }

    private fun `github-pr-factory with`(args: Array<String>) {
        OpenCommand(CommandArgs(
                baseUrl = "",
                repoPath = "",
                userPropertiesFile = "user.properties"
        )).parse(args)
    }

    fun execute(fn: (args: Array<String>) -> Unit, args: Array<String>, `and expect`: String) {
        assertThatThrownBy { fn(args) }
                .hasMessage(`and expect`)
    }

    private fun stubRateLimit(remaining: Int = 5000, resetInMillisSinceEpoch: Long = 1608411669) {
        stubFor(get("/rate_limit").willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{ \n  \"rate\":  {\n    \"limit\": 5000,\n    \"used\": 0,\n    \"remaining\": $remaining,\n    \"reset\": $resetInMillisSinceEpoch\n  }\n}")))
    }
}
