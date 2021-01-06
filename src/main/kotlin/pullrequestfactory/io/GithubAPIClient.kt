package pullrequestfactory.io

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import pullrequestfactory.io.repositories.HttpClient
import java.time.Instant

class GithubAPIClient(
        private val httpClient: HttpClient,
        private val baseUrl: String
) {

    fun get_rate_limit(): RateLimit {
        val res = httpClient.get("$baseUrl/rate_limit")
        val rateLimit = (json_parser().parse(res.text) ?: DefaultRateLimit())
        return rateLimit
    }

    private fun DefaultRateLimit() =
            RateLimit((Rate(limit = 0, remaining = 0, Instant.now(), 0)))

    private fun json_parser(): Klaxon {
        val jsonParser = Klaxon().converter(EpochMilliInstantConverter())
        return jsonParser
    }

}

class EpochMilliInstantConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == Instant::class.java

    override fun toJson(value: Any): Nothing = throw NotImplementedError()

    override fun fromJson(jv: JsonValue): Instant {
        val instant = Instant.ofEpochSecond(jv.int?.toLong() ?: 0)
        return instant
    }

}
