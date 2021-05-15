package it.pullrequestfactory.io.clikt

import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual
import pullrequestfactory.io.programs.impl.InstantSerializer
import pullrequestfactory.io.programs.impl.Rate
import pullrequestfactory.io.programs.impl.RateLimit
import java.time.Instant

fun ensureHighEnoughRateLimit(remaining: Int = 5000, resetInMillisSinceEpoch: Long = 1608411669) {
    stubFor(
        get("/rate_limit").willReturn(
            aResponse()
                .withStatus(200)
                .withBody(
                    Json { serializersModule = SerializersModule { contextual(InstantSerializer) } }.encodeToString(
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
