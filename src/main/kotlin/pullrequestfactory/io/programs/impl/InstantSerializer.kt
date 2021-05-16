package pullrequestfactory.io.programs.impl

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

@ExperimentalSerializationApi
@Serializer(forClass = Instant::class)
object InstantSerializer : KSerializer<Instant> {

    override fun serialize(encoder: Encoder, value: Instant) {
        encoder.encodeString("${value.toEpochMilli()}")
    }

    override fun deserialize(decoder: Decoder): Instant {
        return Instant.ofEpochSecond(decoder.decodeLong())
    }

}
