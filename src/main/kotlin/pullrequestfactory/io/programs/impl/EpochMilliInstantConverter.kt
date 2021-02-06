package pullrequestfactory.io.programs.impl

import com.beust.klaxon.Converter
import com.beust.klaxon.JsonValue
import java.time.Instant

class EpochMilliInstantConverter : Converter {

    override fun canConvert(cls: Class<*>) = cls == Instant::class.java

    override fun toJson(value: Any) = throw NotImplementedError()

    override fun fromJson(jv: JsonValue): Instant {
        return Instant.ofEpochSecond(jv.longValue ?: 0)
    }

}
