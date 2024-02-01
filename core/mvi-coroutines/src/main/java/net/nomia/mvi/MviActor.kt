package net.nomia.mvi

import kotlinx.coroutines.flow.Flow

/**
 * UseCase contains all business logic to produce specific stream of Effects in response to incoming Action
 *
 * There is no need to switch Observables to any particular scheduler as far as MviInteractor will observe Effects
 * on the main thread
 *
 * Sample usage:
 * ```
 *      override fun invoke(action: Action): Effect {
 *          val effectsObservable = when(action) {
 *              is Action.Load -> Observable.just(Effect.Loading, Effect.Loaded)
 *              is Action.Next -> Observable.just(Effect.Submit)
 *          }
 *          return effectsObservable.onErrorReturnItem(Effect.Error)
 *      }
 * ```
 *
 */
fun interface MviActor<Action, Effect, State> : (State, Action) -> Flow<Effect> {
    override fun invoke(previousState: State, action: Action): Flow<Effect>
}
