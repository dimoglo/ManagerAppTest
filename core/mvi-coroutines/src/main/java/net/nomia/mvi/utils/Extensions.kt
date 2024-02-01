package net.nomia.mvi.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn

internal fun <T> Flow<T>.distinctUntilChangedControlled(isEnable : Boolean): Flow<T> {
    return if (isEnable) this.distinctUntilChanged()
    else this
}

internal fun <T : Any> T.logValue(tag : String, method: String): T {
    when (this) {
        is Notification.OnNext<*> -> println("$tag: $method [${Thread.currentThread().name}] > ${this.value}")
        is Notification.OnError<*> -> println("$tag: ${error.message}: $method [${Thread.currentThread().name}] > error")
        else -> println("$tag: $method [${Thread.currentThread().name}] > $this")
    }

    return this
}

internal fun <T> Flow<T>.share(scope : CoroutineScope): Flow<T> {
    return shareIn(
        scope,
        SharingStarted.WhileSubscribed(stopTimeoutMillis = 0, replayExpirationMillis = 0),
        replay = 1
    )
}
