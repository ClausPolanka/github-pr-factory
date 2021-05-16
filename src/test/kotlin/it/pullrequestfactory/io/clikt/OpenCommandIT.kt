package it.pullrequestfactory.io.clikt

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.ClassRule
import org.junit.Test
import pullrequestfactory.domain.PairingPartner
import pullrequestfactory.domain.PairingPartner.*
import pullrequestfactory.domain.branches.Branch
import pullrequestfactory.domain.pullrequests.PullRequest
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.clikt.OpenCommand
import pullrequestfactory.io.programs.impl.InstantSerializer
import pullrequestfactory.io.programs.impl.Rate
import pullrequestfactory.io.programs.impl.RateLimit
import java.time.LocalDateTime
import java.time.ZoneId

class OpenCommandIT {

    private val repoPath = "/repos/ClausPolanka/wordcount"
    private val candidateFirstName = "Firstname"
    private val candidateLastName = "Lastname"

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
        "-pp7", "vaclav"
    )

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
            candidateLastName
        )

        stubFor(
            get(urlPathMatching("$repoPath/branches(\\?page\\=1)?"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(Json {
                            serializersModule = SerializersModule { contextual(InstantSerializer) }
                        }.encodeToString(branches))
                )
        )

        `github-pr-factory OPEN pull requests`(
            arrayOf(
                "-g", "any-github-token",
                "-fn", candidateFirstName,
                "-ln", candidateLastName,
                "-pp1", MARKUS.pullRequestName(),
                "-pp2", BERNI.pullRequestName(),
                "-pp3", LUKAS.pullRequestName(),
                "-pp4", JAKUB.pullRequestName(),
                "-pp5", PETER.pullRequestName(),
                "-pp6", CHRISTIAN.pullRequestName(),
                "-pp7", VACLAV.pullRequestName()
            )
        )

        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 1 / Session 1 Markus [PR]",
                Branch("master"),
                branches[0]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 2 / Session 2 Berni [PR]",
                branches[0],
                branches[1]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 3 / Session 3 Lukas [PR]",
                branches[1],
                branches[2]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 4 / Session 4 Jakub [PR]",
                branches[2],
                branches[3]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 5 / Session 5 Peter [PR]",
                branches[3],
                branches[4]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 6 / Session 6 Christian [PR]",
                branches[4],
                branches[5]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 7 / Session 7 Vaclav",
                branches[5],
                branches[6]
            )
        )
    }

    @Test
    fun `OPEN pull requests where last session is considered to be finished`() {
        ensureHighEnoughRateLimit()

        val branches = branchesFor(
            listOf(MARKUS, BERNI, LUKAS, JAKUB, PETER, CHRISTIAN, VACLAV),
            candidateFirstName,
            candidateLastName
        )

        stubFor(
            get(urlPathMatching("$repoPath/branches(\\?page\\=1)?"))
                .willReturn(
                    aResponse()
                        .withStatus(200)
                        .withBody(Json {
                            serializersModule = SerializersModule { contextual(InstantSerializer) }
                        }.encodeToString(branches))
                )
        )

        `github-pr-factory OPEN pull requests`(
            arrayOf(
                "--last-finished",
                "-g", "any-github-token",
                "-fn", candidateFirstName,
                "-ln", candidateLastName,
                "-pp1", MARKUS.pullRequestName(),
                "-pp2", BERNI.pullRequestName(),
                "-pp3", LUKAS.pullRequestName(),
                "-pp4", JAKUB.pullRequestName(),
                "-pp5", PETER.pullRequestName(),
                "-pp6", CHRISTIAN.pullRequestName(),
                "-pp7", VACLAV.pullRequestName()
            )
        )

        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 1 / Session 1 Markus [PR]",
                Branch("master"),
                branches[0]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 2 / Session 2 Berni [PR]",
                branches[0],
                branches[1]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 3 / Session 3 Lukas [PR]",
                branches[1],
                branches[2]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 4 / Session 4 Jakub [PR]",
                branches[2],
                branches[3]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 5 / Session 5 Peter [PR]",
                branches[3],
                branches[4]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 6 / Session 6 Christian [PR]",
                branches[4],
                branches[5]
            )
        )
        verify(
            PullRequest(
                "$candidateFirstName $candidateLastName Iteration 7 / Session 7 Vaclav [PR]",
                branches[5],
                branches[6]
            )
        )
    }

    @Test
    fun `OPEN pull requests where rate limit is not sufficient to fulfil the request`() {
        stubFor(
            get("/rate_limit").willReturn(
                aResponse()
                    .withStatus(403)
                    .withBody(
                        Json { serializersModule = SerializersModule { contextual(InstantSerializer) } }.encodeToString(
                            RateLimit(
                                Rate(
                                    limit = 50,
                                    used = 50,
                                    remaining = 0,
                                    reset = fromNowInOneHour()
                                )
                            )
                        )
                    )
            )
        )

        val actualOutputCapture = mutableListOf<String>()

        OpenCommand(argsWith(fakeUI(actualOutputCapture))).parse(anyArgs.withOption("--debug"))

        assertThat(actualOutputCapture.any { it.contains("remaining=0") }).isTrue
    }

    private fun branchesFor(
        pairingPartner: List<PairingPartner>,
        fn: String,
        ln: String
    ) = pairingPartner
        .map { it.name.toLowerCase() }
        .mapIndexed { i, pp -> Branch("${fn}_${ln}_iteration_${i.inc()}_$pp") }
        .toTypedArray()

    private fun `github-pr-factory OPEN pull requests`(args: Array<String>) {
        OpenCommand(cmdArgsFor(repoPath)).parse(args)
    }

    private fun verify(pr: PullRequest) {
        val json = """{"base" : "${pr.base.name}", "head" : "${pr.head.name}", "title" : "${pr.title}"}"""
        verify(
            postRequestedFor(urlMatching("$repoPath/pulls"))
                .withRequestBody(equalToJson(json))
                .addCommonHeaders()
        )
    }

    private fun fromNowInOneHour() =
        LocalDateTime.now().plusHours(1).atZone(ZoneId.systemDefault()).toInstant()

    private fun argsWith(ui: UI) = cmdArgsFor(repoPath, ui)

    private fun fakeUI(outputCapture: MutableList<String>) =
        object : UI {
            override fun show(msg: String) {
                outputCapture.add(msg)
            }

            override fun getUserInput(msg: String): String {
                TODO("Ignore")
            }
        }
}

private inline fun <reified T> Array<T>.withOption(option: T): Array<T> {
    val elems = this.toMutableList()
    elems.add(option)
    return elems.toTypedArray()
}
