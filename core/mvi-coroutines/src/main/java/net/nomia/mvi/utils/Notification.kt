package net.nomia.mvi.utils

/**
 * Analogue for RxJava's Notification class for materialize / dematerialize operators
 *
 * There is no 'materialize / dematerialize' operators in Flow
 * https://github.com/Kotlin/kotlinx.coroutines/issues/2092
 */
sealed class Notification<T> {
    data class OnNext<T>(val value: T) : Notification<T>()
    data class OnError<T>(val error: Throwable) : Notification<T>()
}
