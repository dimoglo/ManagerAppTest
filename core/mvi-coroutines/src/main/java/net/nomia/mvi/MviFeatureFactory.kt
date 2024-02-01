package net.nomia.mvi

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.emptyFlow

/**
 * Factory to create MviFeature based on coroutine scope
 */
open class MviFeatureFactory<Action : Any, Effect : Any, State : Any, Event : Any>(
    private val initialState: State,
    private val bootstrap: MviBootstrap<Effect> = MviBootstrap { emptyFlow() },
    private val actor: MviActor<Action, Effect, State>,
    private val eventProducer: EventProducer<Effect, Event> = EventProducer { null },
    private val reducer: MviReducer<Effect, State>,
    private val tagPostfix: String = "Feature",
    private val isEnableDistinctUntilChanged : Boolean = true
) {

    /**
     * @param shareScope - the coroutine scope that will be used for sharing feature' state
     */
    open fun create(shareScope: CoroutineScope): MviFeature<Action, Effect, State, Event> {
        return MviFeature(
            initialState,
            bootstrap,
            actor,
            eventProducer,
            reducer,
            tagPostfix,
            shareScope,
            isEnableDistinctUntilChanged
        )
    }
}
