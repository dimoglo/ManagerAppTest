package net.nomia.core.common.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.time.Instant

object InstantAsLongSerializer : KSerializer<Instant> {

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Instant", PrimitiveKind.LONG)

    override fun serialize(encoder: Encoder, value: Instant) {
        val longValue = value.toEpochMilli()
        encoder.encodeLong(longValue)
    }

    override fun deserialize(decoder: Decoder): Instant {
        val longValue = decoder.decodeLong()
        return Instant.ofEpochMilli(longValue)
    }

}
