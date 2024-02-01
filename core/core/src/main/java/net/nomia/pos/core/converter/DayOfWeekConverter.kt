package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.time.DayOfWeek

class DayOfWeekConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: DayOfWeek?): Int? {
            return when (value) {
                DayOfWeek.SUNDAY -> 0
                else -> value?.value
            }
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: Int?): DayOfWeek? {
            return when (value) {
                0 -> DayOfWeek.SUNDAY
                else -> value?.let(DayOfWeek::of)
            }
        }
    }
}
