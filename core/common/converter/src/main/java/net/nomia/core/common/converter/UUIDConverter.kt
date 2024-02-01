package net.nomia.core.common.converter

import androidx.room.TypeConverter
import java.util.UUID

interface UUIDConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromUUID(value: UUID?): String? {
            return value?.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toUUID(value: String?): UUID? {
            return value?.let(UUID::fromString)
        }
    }
}
