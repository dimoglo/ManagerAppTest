package net.nomia.core.common.converter

import androidx.room.TypeConverter
import java.net.InetAddress
import java.net.InetSocketAddress

interface InetSocketAddressConverter {

    companion object {
        @TypeConverter
        @JvmStatic
        fun fromInstant(value: InetSocketAddress): String {
            val address = value.address.address.joinToString(".")
            val port = value.port
            return "$address:$port"
        }

        @TypeConverter
        @JvmStatic
        fun toInstant(value: String): InetSocketAddress {
            val address = value
                .split(":")
                .first()
                .split(".")
                .map { it.toInt().toByte() }
                .toByteArray()
                .let(InetAddress::getByAddress)

            val port = value
                .split(":")
                .last()
                .toIntOrNull() ?: 0

            return InetSocketAddress(address, port)
        }
    }
}
