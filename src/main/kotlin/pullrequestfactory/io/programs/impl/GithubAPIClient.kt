package pullrequestfactory.io.programs.impl

import com.beust.klaxon.Klaxon
import pullrequestfactory.io.repositories.HttpClient
import java.time.Instant

class GithubAPIClient(
        private val httpClient: HttpClient,
        private val baseUrl: String
) {

    fun get_rate_limit(): RateLimit {
        val res = httpClient.get("$baseUrl/rate_limit")
        val rateLimit = when (res.statusCode) {
            401, 403, 404 -> DefaultRateLimit()
            else -> { (json_parser().parse(res.text) ?: DefaultRateLimit()) }
        }
        return rateLimit
    }

    private fun DefaultRateLimit() =
            RateLimit((Rate(limit = 0, remaining = 0, Instant.now(), 0)))

    private fun json_parser(): Klaxon {
        val jsonParser = Klaxon().converter(EpochMilliInstantConverter())
        return jsonParser
    }

}
