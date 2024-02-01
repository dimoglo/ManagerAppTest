package net.nomia.core.common.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

interface LocalDateTimeConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromLocalDateTime(value: LocalDateTime?): Long? {
            return value?.atZone(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        }

        @TypeConverter
        @JvmStatic
        fun toLocalDateTime(value: Long?): LocalDateTime? {
            return value?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDateTime()
            }
        }
    }
}
