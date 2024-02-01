package net.nomia.main.config

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import net.nomia.main.data.local.dao.EmployeeDao
import net.nomia.main.data.local.entity.EmployeeEntity
import net.nomia.main.data.local.entity.EmployeeGroupEntity
import net.nomia.pos.core.converter.BigDecimalConverter
import net.nomia.pos.core.converter.CurrencyConverter
import net.nomia.pos.core.converter.DateConverter
import net.nomia.pos.core.converter.DayOfWeekConverter
import net.nomia.pos.core.converter.InstantConverter
import net.nomia.pos.core.converter.LocalDateConverter
import net.nomia.pos.core.converter.LocalTimeConverter
import net.nomia.pos.core.converter.MeasureUnitConverter
import net.nomia.pos.core.converter.UUIDConverter

@Database(
    entities = [
        EmployeeEntity::class,
        EmployeeGroupEntity::class
    ],
    version = 1
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
        DayOfWeekConverter::class,
        MeasureUnitConverter::class
    ]
)
abstract class MainDatabase : RoomDatabase() {
    abstract fun employeeDao(): EmployeeDao
}
