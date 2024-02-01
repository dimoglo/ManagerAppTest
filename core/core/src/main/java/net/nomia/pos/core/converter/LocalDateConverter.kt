package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class LocalDateConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: LocalDate?): Long? {
            return value?.atStartOfDay(ZoneId.systemDefault())?.toInstant()?.toEpochMilli()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long?): LocalDate? {
            return value?.let {
                Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).toLocalDate()
            }
        }
    }
}
