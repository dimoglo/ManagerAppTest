package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import tech.units.indriya.AbstractUnit
import javax.measure.Unit

class MeasureUnitConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Unit<*>?): String? {
            return value?.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: String?): Unit<*>? {
            return value?.let { AbstractUnit.parse(it) }
        }
    }
}
