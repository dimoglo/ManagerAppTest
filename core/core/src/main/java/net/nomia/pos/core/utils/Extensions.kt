package net.nomia.pos.core.utils

fun Int.positiveOrZero(): Int = if (this < 0) 0 else this
