package pullrequestfactory.io.programs.impl

import com.beust.klaxon.Klaxon
import pullrequestfactory.io.repositories.HttpClient
import java.time.Instant

class GithubAPIClient(
    private val httpClient: HttpClient,
    private val baseUrl: String
) {

    fun getRateLimit(): RateLimit {
        val res = httpClient.get("$baseUrl/rate_limit")
        return when (res.statusCode) {
            401, 403, 404 -> defaultRateLimit()
            else -> {
                (jsonParser().parse(res.text) ?: defaultRateLimit())
            }
        }
    }

    private fun defaultRateLimit() =
        RateLimit((Rate(limit = 0, remaining = 0, Instant.now(), 0)))

    private fun jsonParser(): Klaxon =
        Klaxon().converter(EpochMilliInstantConverter())

}
