package net.nomia.mvi

import kotlinx.coroutines.flow.Flow

/**
 * Implement this interface to alter Effect producing by UseCase
 *
 * Doesn't operate on particular scheduler.
 * Invoke() can be called on any thread
 */
fun interface EffectInterceptor<Effect : Any> : (Flow<Effect>) -> Flow<Effect> {
    override fun invoke(incomeFlow: Flow<Effect>): Flow<Effect>
}
