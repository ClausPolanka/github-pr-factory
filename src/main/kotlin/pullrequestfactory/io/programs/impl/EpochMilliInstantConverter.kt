package pullrequestfactory.io.programs.impl

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import java.time.Instant

class EpochMilliInstantConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == Instant::class.java

    override fun toJson(value: Any): Nothing = throw NotImplementedError()

    override fun fromJson(jv: JsonValue): Instant {
        val instant = Instant.ofEpochSecond(jv.int?.toLong() ?: 0)
        return instant
    }

}
