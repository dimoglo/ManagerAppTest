package net.nomia.mvi

import kotlinx.coroutines.flow.Flow

/**
 * MviBootstrap used to provide initial stream of effects.
 *
 * It is useful when some data preload should be performed when clients are subscribed to MviInteractor.
 *
 * Replacement for sending implicit action to MviInteractor, such as
 *
 * ```
 * mviInteractor.accept(Action.LoadData)
 *
 * ```
 * There is no need to switch Observable to any particular scheduler as far as MviInteractor will observe Effects
 * on the main thread
 *
 * Sample usage:
 * ```
 *
 *      override fun invoke(): Observable<Effect> {
 *          return repository.loadData().map { Effect.Data(it) }
 *      }
 * ```
 *
 */
fun interface MviBootstrap<Effect> : () -> Flow<Effect> {
    override fun invoke(): Flow<Effect>
}
