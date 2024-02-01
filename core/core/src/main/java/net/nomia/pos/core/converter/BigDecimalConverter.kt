package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

class BigDecimalConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromModel(value: BigDecimal?): Long? {
            return try {
                value?.multiply(BigDecimal.valueOf(10000))?.setScale(0, RoundingMode.HALF_EVEN)
                    ?.longValueExact()
            } catch (ex: Exception) {
                Timber.e(ex)
                0L
            }
        }

        @TypeConverter
        @JvmStatic
        fun toModel(value: Long?): BigDecimal? {
            return value?.let { BigDecimal(it) }
                ?.divide(BigDecimal(10000), 4, RoundingMode.HALF_EVEN)
        }
    }
}
