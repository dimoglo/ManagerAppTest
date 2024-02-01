package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.time.LocalTime

class LocalTimeConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: LocalTime?): Long? {
            return value?.toNanoOfDay()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long?): LocalTime? {
            return value?.let {
                LocalTime.ofNanoOfDay(it)
            }
        }
    }
}
