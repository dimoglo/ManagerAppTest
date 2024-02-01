package net.nomia.mvi

/**
 * Reducer take previous State and new Effect and produce new State.
 *
 * In case of usage inside of MviInteractor invocation will be always on the main thread.
 *
 * There is no obligation to use previousState, it can be completely ignored
 *
 * Sample usage:
 * ```
 *
 *      override fun invoke(previousState: State, effect: Effect): Effect {
 *          when(effect) {
 *              is Action.Loading -> State.Loading
 *              is Action.Loaded -> previousState.copy(data = effect.data)
 *              is Action.Error -> previousState.copy(data = null, error = true)
 *          }
 *      }
 *
 * ```
 */

fun interface MviReducer<Effect, State> {
    suspend fun invoke(previousState: State, effect: Effect): State
}
