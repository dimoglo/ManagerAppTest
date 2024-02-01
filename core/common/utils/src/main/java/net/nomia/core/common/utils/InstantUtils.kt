package net.nomia.core.common.utils

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

fun Instant.asLocalDate(): LocalDate = this.atZone(ZoneId.systemDefault()).toLocalDate()


fun Instant?.convertByPattern(pattern: String = "d MMM HH:mm"): String =
    SimpleDateFormat(pattern, Locale("ru")).format(Date.from(this ?: Instant.now()))
