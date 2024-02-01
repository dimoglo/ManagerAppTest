package net.nomia.common.ui.extensions

import java.math.BigDecimal
import java.text.NumberFormat

fun BigDecimal.percentFormattedValue(): String =
    percentFormat.format(this)

private val percentFormat: NumberFormat = NumberFormat.getInstance().apply {
    minimumFractionDigits = 0
}
