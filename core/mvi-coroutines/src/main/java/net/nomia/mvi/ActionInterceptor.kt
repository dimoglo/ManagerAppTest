package net.nomia.mvi

import kotlinx.coroutines.flow.Flow

/**
 * ActionInterceptor is used to apply some transformation to event of actions.
 * It is useful when you want to filter some actions of distinct them by some criteria.
 *
 * Sample usage
 * ```
 * override fun apply(upstream: Observable<Action>): ObservableSource<Action> {
 *     return upstream.distinctUntilChanged { previous, current -> current is previous }
 * }
 */
fun interface ActionInterceptor<Action : Any> : (Flow<Action>) -> Flow<Action> {
    override fun invoke(incomeFlow: Flow<Action>): Flow<Action>
}
