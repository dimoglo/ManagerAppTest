package net.nomia.common.ui.extensions

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.format.TextStyle
import java.time.temporal.ChronoField

val timePartFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.HOUR_OF_DAY, 2)
    .appendLiteral(':')
    .appendValue(ChronoField.MINUTE_OF_HOUR, 2)
    .toFormatter()

val datePartTextFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.DAY_OF_MONTH, 2)
    .appendLiteral(' ')
    .appendText(ChronoField.MONTH_OF_YEAR, TextStyle.FULL)
    .toFormatter()

val datePartNumericFormatter: DateTimeFormatter = DateTimeFormatterBuilder()
    .appendValue(ChronoField.DAY_OF_MONTH, 2)
    .appendLiteral('.')
    .appendValue(ChronoField.MONTH_OF_YEAR, 2)
    .toFormatter()

fun Instant.asTimeString(): String = this.atZone(ZoneId.systemDefault()).format(timePartFormatter)
fun Instant.asDateTextString(): String = this.atZone(ZoneId.systemDefault()).format(datePartTextFormatter)
fun Instant.asDateNumericString(): String = this.atZone(ZoneId.systemDefault()).format(datePartNumericFormatter)
