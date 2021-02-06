package it.pullrequestfactory.io.clikt

import com.beust.klaxon.Klaxon
import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.matching.RequestPatternBuilder
import pullrequestfactory.io.programs.impl.Rate
import pullrequestfactory.io.programs.impl.RateLimit
import java.time.Instant

fun ensureHighEnoughRateLimit(remaining: Int = 5000, resetInMillisSinceEpoch: Long = 1608411669) {
    stubFor(
        WireMock.get("/rate_limit").willReturn(
            aResponse()
                .withStatus(200)
                .withBody(
                    Klaxon().toJsonString(
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

fun <T> toJson(objects: Array<T>) = Klaxon().toJsonString(objects)

fun RequestPatternBuilder.addCommonHeaders(): RequestPatternBuilder? {
    return this.withHeader("Accept", WireMock.matching("application/json"))
        .withHeader("Authorization", WireMock.matching("token .*"))
        .withHeader("Content-Type", WireMock.matching("application/json"))
}
