package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {

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
