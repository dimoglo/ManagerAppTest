package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.util.Currency

class CurrencyConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: Currency): String {
            return value.toString()
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: String): Currency {
            return Currency.getInstance(value)
        }
    }
}
