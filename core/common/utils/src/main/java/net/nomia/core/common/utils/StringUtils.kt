package net.nomia.core.common.utils

inline fun <R> String.ifNotBlank(transform: (String) -> R): R? =
    if (isNotBlank()) transform(this) else null


fun String.toBigDecimalHandleCommaOrNull() = this.replace(',','.').toBigDecimalOrNull()
