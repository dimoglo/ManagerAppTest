package net.nomia.pos.core.data

object RegexPattern {
    /*
    TOTAL_AMOUNT:
        LIMITED - used to represent total amount input,
        where integers can vary from 0 to 10 and decimal part from 0 to 2
        UNLIMITED - same as LIMITED, but integers are not restricted
    */
    val LIMITED_TOTAL_AMOUNT_INPUT = "^(([1-9](\\d{0,9})|0)([.|,]\\d{0,2})?)?$".toRegex()
    val UNLIMITED_TOTAL_AMOUNT_INPUT = "^(([1-9](\\d*)|0)([.|,]\\d{0,2})?)?$".toRegex()

    /*
    QUANTITY_INTEGERS:
        LIMITED - used to represent quantity of anything input, where upper bound is 10 digits
        UNLIMITED - same as LIMITED, but without upper bound
    */
    val LIMITED_QUANTITY_INTEGERS_INPUT = "^(([1-9]\\d{0,10})|0)?$".toRegex()
    val UNLIMITED_QUANTITY_INTEGERS_INPUT = "^(([1-9]\\d*)|0)?$".toRegex()

    /*
    QUANTITY_DECIMAL:
        LIMITED - used to represent quantity of Unit.LITRE or UNIT.KILOGRAM
        with integers from 0 to 10 and scale from 0 to 3
        UNLIMITED - same as LIMITED, but integers are not restricted
    */
    val UNLIMITED_QUANTITY_DECIMAL_INPUT = "^(([1-9](\\d*)|0)([.|,]\\d{0,3})?)?$".toRegex()
    val LIMITED_QUANTITY_DECIMAL_INPUT = "^(([1-9](\\d{0,10})|0)([.|,]\\d{0,3})?)?$".toRegex()
}
