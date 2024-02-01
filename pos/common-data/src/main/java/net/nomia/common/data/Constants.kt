package net.nomia.common.data

import kotlinx.coroutines.flow.SharingStarted
import kotlin.time.Duration.Companion.seconds

object Constants {
    const val SearchDelay = 300L
    const val RETRY_TIMEOUT_SYNC = 5000L
    const val MAX_NUMBER_ATTEMPTS = 3
    const val CONTENT_ANIMATION_DURATION = 300
    const val CASH_REGISTRY_DOCUMENTS_TIMEOUT_SYNC_MILLISECONDS: Long = 5 * 60 * 1000

    val SharingStartedWithDefaultTimeout = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds)
}
