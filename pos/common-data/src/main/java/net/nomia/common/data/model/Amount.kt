package net.nomia.common.data.model

import net.nomia.common.data.CurrencyHelper
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.Currency

data class Amount(
    var value: BigDecimal, val currency: Currency
) : Comparable<Amount> {

    constructor(value: Amount) : this(value.value, value.currency)

    constructor(value: Long, currency: Currency) : this(BigDecimal.valueOf(value), currency)

    operator fun times(quantity: BigDecimal) = Amount(value * quantity, currency)

    operator fun times(amount: Amount) = Amount(value * amount.value, currency)

    fun times(unitQuantity: Measure, quantity: Measure): Amount {
        return this * (quantity.value.value.toDouble().toBigDecimal()
            .divide(unitQuantity.value.value.toDouble().toBigDecimal(), 3, RoundingMode.HALF_EVEN))
    }

    operator fun div(quantity: Measure): Amount {
        return copy(value = value / quantity.value())
    }


    operator fun plus(amount: BigDecimal) = Amount(value + amount, currency)

    operator fun plus(amount: Amount?) =
        Amount(value + (amount?.value ?: BigDecimal.ZERO), currency)

    operator fun minus(amount: BigDecimal) = Amount(value - amount, currency)

    operator fun minus(amount: Amount?) =
        Amount(value - (amount?.value ?: BigDecimal.ZERO), currency)

    // TODO: doesn't account for currency yet
    override fun compareTo(other: Amount) = if (currency == other.currency) {
        value.compareTo(other.value)
    } else {
        throw IllegalArgumentException("Only amounts of same currencies can be compared")
    }

    override fun toString(): String {
        return CurrencyHelper.getCurrencyFormatter(currency)
            .format(value)
            .replace(".00", "")
    }

    companion object {
        fun zero(currency: Currency) = Amount(BigDecimal.ZERO, currency)
    }

}
