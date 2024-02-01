package net.nomia.mvi.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * There is no 'materialize' operator in Flow
 * https://github.com/Kotlin/kotlinx.coroutines/issues/2092
 */
fun <T> Flow<T>.materialize(): Flow<Notification<T>> {
    return map<T, Notification<T>> { value ->
        Notification.OnNext(value)
    }.catch { error ->
        emit(Notification.OnError(error))
    }
}

/**
 * There is no 'dematerialize' operator in Flow
 * https://github.com/Kotlin/kotlinx.coroutines/issues/2092
 */
fun <T> Flow<Notification<T>>.dematerialize(): Flow<T> {
    return map { notification ->
        when (notification) {
            is Notification.OnNext<T> -> notification.value
            is Notification.OnError<*> -> throw notification.error
        }
    }
}
