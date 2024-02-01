package net.nomia.common.data

import com.google.i18n.phonenumbers.AsYouTypeFormatter
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat

object PhoneHelper {

    private const val DefaultCountryCode = "RU"
    private val phoneNumberUtil = PhoneNumberUtil.getInstance()
    val phoneNumberTypeFormatter: AsYouTypeFormatter = phoneNumberUtil.getAsYouTypeFormatter(DefaultCountryCode)

    fun isValidPhoneNumber(
        rawPhone: String,
        countryCode: String = DefaultCountryCode,
    ): Boolean =
        try {
            val phoneNumber = phoneNumberUtil.parse(rawPhone, countryCode)
            (phoneNumberUtil.isPossibleNumber(rawPhone, countryCode)
                    && phoneNumberUtil.isValidNumberForRegion(phoneNumber, countryCode))
        } catch (ex: Exception) {
            false
        }

    /*
    *  converts inputs like +XXXXXXXXXXX to outputs like this +X XXX XXX-XX-XX
    */
    fun formatPhone(rawPhone: String): String = format(rawPhone, PhoneNumberFormat.INTERNATIONAL)

    /*
    *  same as @formatPhone method, but conversely
    */
    fun normalizePhone(rawPhone: String): String = format(rawPhone, PhoneNumberFormat.E164)

    @Suppress("SwallowedException")
    private fun format(rawPhone: String, numberFormat: PhoneNumberFormat): String =
        try {
            val parsedPhoneNumber = phoneNumberUtil.parse(rawPhone, DefaultCountryCode)
            phoneNumberUtil.format(
                parsedPhoneNumber,
                numberFormat
            )
        } catch (ex: Exception) {
            rawPhone
        }

}
