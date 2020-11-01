package pullrequestfactory.io

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import com.beust.klaxon.Klaxon
import pullrequestfactory.io.repositories.HttpClient
import pullrequestfactory.io.repositories.KhttpClient
import java.time.Instant

class GithubAPIClient(private val httpClient: HttpClient) {

    fun get_rate_limit(): RateLimit? {
        val res = httpClient.get("https://api.github.com/rate_limit")
        val rateLimit: RateLimit? = json_parser().parse(res.text)
        return rateLimit
    }

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

fun main() {
    println(GithubAPIClient(KhttpClient()).get_rate_limit())
}
