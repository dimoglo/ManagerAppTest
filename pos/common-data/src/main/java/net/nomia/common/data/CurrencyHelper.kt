package net.nomia.common.data

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale
import java.util.concurrent.ConcurrentHashMap

object CurrencyHelper {

    private val LocalRu = Locale("ru", "RU")

    private val currencyFormatter = ConcurrentHashMap<String, Pair<Locale, NumberFormat>>().apply {
        put("RUB", LocalRu to NumberFormat.getCurrencyInstance(LocalRu).apply {
            minimumFractionDigits = 0
        })
    }

    private val printerCurrencyFormatter = ConcurrentHashMap<String, NumberFormat>().apply {
        put("RUB", (NumberFormat.getCurrencyInstance(LocalRu) as DecimalFormat).apply {
            minimumFractionDigits = 0
            decimalFormatSymbols = decimalFormatSymbols.apply {
                currencySymbol = "руб"

            }
        })
    }

    private fun getCurrencyPair(currency: Currency): Pair<Locale, NumberFormat> {
        if (currencyFormatter.containsKey(currency.currencyCode)) {
            return currencyFormatter[currency.currencyCode]!!
        } else {
            val locales = NumberFormat.getAvailableLocales()

            for (locale in locales) {
                val formatter = NumberFormat.getCurrencyInstance(locale)
                if (formatter.currency == currency) {
                    currencyFormatter[currency.currencyCode] = locale to formatter
                    return currencyFormatter[currency.currencyCode]!!
                }
            }
            throw IllegalStateException("Unsupported currency format")
        }
    }

    fun getCurrencyFormatter(currency: Currency): NumberFormat {
        return getCurrencyPair(currency).second
    }

    fun getCurrencyPrinterFormatter(currency: Currency): NumberFormat {
        return printerCurrencyFormatter.getOrDefault(
            currency.currencyCode,
            NumberFormat.getCurrencyInstance(Locale.getDefault())
        )
    }

    fun getLocale(currency: Currency): Locale {
        return getCurrencyPair(currency).first
    }
}
