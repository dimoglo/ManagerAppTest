package net.nomia.settings.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.nomia.pos.core.converter.BigDecimalConverter
import net.nomia.pos.core.converter.CurrencyConverter
import net.nomia.pos.core.converter.DateConverter
import net.nomia.pos.core.converter.InetAddressConverter
import net.nomia.pos.core.converter.InstantConverter
import net.nomia.pos.core.converter.LocalDateConverter
import net.nomia.pos.core.converter.LocalTimeConverter
import net.nomia.pos.core.converter.UUIDConverter
import net.nomia.settings.data.local.dao.SettingsDao
import net.nomia.settings.data.local.dao.TerminalDao
import net.nomia.settings.data.local.entity.SettingsEntity
import net.nomia.settings.data.local.entity.TerminalEntity

@Database(
    entities = [
        SettingsEntity::class,
        TerminalEntity::class,
    ],
    version = 1,
)
@TypeConverters(
    value = [
        BigDecimalConverter::class,
        CurrencyConverter::class,
        InstantConverter::class,
        LocalDateConverter::class,
        LocalTimeConverter::class,
        DateConverter::class,
        UUIDConverter::class,
        InetAddressConverter::class
    ]
)
abstract class SettingsDatabase : RoomDatabase() {
    abstract fun settingsDao(): SettingsDao
    abstract fun terminalDao(): TerminalDao
}
