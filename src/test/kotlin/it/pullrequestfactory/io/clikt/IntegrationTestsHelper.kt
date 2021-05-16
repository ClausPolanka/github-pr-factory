package it.pullrequestfactory.io.clikt

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import pullrequestfactory.domain.uis.QuietUI
import pullrequestfactory.domain.uis.UI
import pullrequestfactory.io.clikt.CommandArgs
import pullrequestfactory.io.programs.impl.InstantSerializer
import pullrequestfactory.io.programs.impl.Rate
import pullrequestfactory.io.programs.impl.RateLimit
import pullrequestfactory.jsonSerializer
import java.time.Instant

@ExperimentalSerializationApi
fun ensureHighEnoughRateLimit(remaining: Int = 5000, resetInMillisSinceEpoch: Long = 1608411669) {
    stubFor(
        get("/rate_limit").willReturn(
            aResponse()
                .withStatus(200)
                .withBody(
                    jsonSerializer().encodeToString(
                        RateLimit(
                            Rate(
                                limit = 5000,
                                used = 0,
                                remaining = remaining,
                                reset = Instant.ofEpochMilli(resetInMillisSinceEpoch)
                            )
                        )
                    )
                )
        )
    )
}

fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
    return this.withHeader("Accept", matching("application/json"))
        .withHeader("Authorization", matching("token .*"))
        .withHeader("Content-Type", matching("application/json"))
}

@ExperimentalSerializationApi
fun cmdArgsFor(repoPath: String, ui: UI = QuietUI()) = CommandArgs(
    baseUrl = "http://localhost:8080",
    repoPath = repoPath,
    userPropertiesFile = "user.properties",
    ui = ui,
    jsonSerizalizer = Json { serializersModule = SerializersModule { contextual(InstantSerializer) } }
)
