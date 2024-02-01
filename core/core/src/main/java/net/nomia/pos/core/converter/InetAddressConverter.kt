package net.nomia.pos.core.converter

import androidx.room.TypeConverter
import java.net.InetAddress

class InetAddressConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: InetAddress?): String? {
            return value?.address?.joinToString(".")
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: String?): InetAddress? {
            return value?.split(".")?.map {
                it.toInt().toByte()
            }?.toByteArray()?.let(InetAddress::getByAddress)
        }
    }
}
