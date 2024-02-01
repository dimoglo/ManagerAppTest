package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.time.Instant

class InstantConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Instant?): Long? {
            return value?.toEpochMilli()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long?): Instant? {
            return value?.let(Instant::ofEpochMilli)
        }
    }
}
