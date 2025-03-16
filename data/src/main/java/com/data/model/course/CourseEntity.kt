package com.data.model.course

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.UUID


object UUIDSerializer : KSerializer<UUID> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: UUID) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): UUID {
        return UUID.fromString(decoder.decodeString())
    }
}


@Serializable
data class CourseEntity(
    @Serializable(with = UUIDSerializer::class) val id: UUID,
    val sourceLanguage: String,
    val targetLanguage: String,
    @Serializable(with = UUIDSerializer::class) val allWordsId: UUID? = null,
    @Serializable(with = UUIDSerializer::class) val selectedSetId: UUID? = null,
)