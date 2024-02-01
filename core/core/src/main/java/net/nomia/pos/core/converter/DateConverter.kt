package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.util.Date

class DateConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Date?): Long? {
            return value?.time
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: Long?): Date? {
            return value?.let(::Date)
        }
    }
}
