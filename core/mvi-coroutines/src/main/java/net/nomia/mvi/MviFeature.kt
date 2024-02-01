package net.nomia.mvi

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.AbstractFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import net.nomia.mvi.utils.Notification
import net.nomia.mvi.utils.distinctUntilChangedControlled
import net.nomia.mvi.utils.logValue
import net.nomia.mvi.utils.materialize
import net.nomia.mvi.utils.share
import java.util.concurrent.atomic.AtomicInteger


/**
 * Base implementation for MVI pattern
 * On calling side actions stream `Observable<Action>` should be provided:
 * ```
 *
 *      actions.subscribe(mviInteractor)
 *      mviInteractor.subscribe { state -> /* consume state */ }
 *
 * ```
 *
 * Reducer invocation always occurs on the main thread. No need to intentionally switch threads inside of useCase
 * or at the calling side.
 *
 * A result of EffectListener's invocation will be send to the `events` stream
 *
 * Subscribe to the `events` stream to consume one-time events, not related to the State. Example of such events are:
 * toasts, notifications, signals.
 * Events doesn't emit on particular thread. Thread-switching remains up to client.
 *
 *  ```
 *
 *      mviInteractor.events.subscribe { event -> vm.handleEvent(...) }
 *
 * ```
 *
 * LOG_EXTENDED can be used to enable additional logging for each downstream phase
 *
 * @param initialState will be used as first state. State will be updated after Effect reducing
 * @param actor will return Observable<Effect> as a response to incoming Action
 * @param reducer will convert Effect to final State with a help of previous value
 * @param bootstrap allows to perform initial setup, i.e. load data
 * @param eventProducer produces on-time event without side-effects based on Effect (or null if no event required)
 * @param tagPostfix provides ability to set specific tag to distinguish different MviInteractors in logcat
 *
 */
open class MviFeature<Action : Any, Effect : Any, State : Any, Event : Any>(
    private val initialState: State,
    private val bootstrap: MviBootstrap<Effect> = MviBootstrap { emptyFlow() },
    private val actor: MviActor<Action, Effect, State>,
    private val eventProducer: EventProducer<Effect, Event> = EventProducer { null },
    private val reducer: MviReducer<Effect, State>,
    private val tagPostfix: String = "Feature",
    shareScope: CoroutineScope,
    isEnableDistinctUntilChanged: Boolean = true
) : AbstractFlow<Notification<State>>() {

    companion object {

        private const val TAG_PREFIX = "Mvi-"

        private const val ACTIONS_BUFFER_SIZE = 10

        private const val EVENTS_BUFFER_SIZE = 10

        var useExtendedLogs = true

        var stateFlowDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate

        var effectFlowDispatcher: CoroutineDispatcher = Dispatchers.Main
    }

    private val tag = "$TAG_PREFIX$tagPostfix"

    private val subscribers = AtomicInteger(0)

    val hasSubscribers get() = subscribers.get() > 0

    var state = initialState
        private set

    private val actionsFlow = MutableSharedFlow<Action>(replay = 0, ACTIONS_BUFFER_SIZE, BufferOverflow.SUSPEND)

    private val eventsFlow = MutableSharedFlow<Event>(replay = 0, EVENTS_BUFFER_SIZE, BufferOverflow.SUSPEND)

    private val actionCacheFlow = MutableSharedFlow<List<Action>>(replay = 1, 1, BufferOverflow.SUSPEND)

    val events: Flow<Event?> = eventsFlow.asSharedFlow()

    private val stateFlow: Flow<Notification<State>> =
        merge(observeBootstrap(), observeActions())
            .flowOn(effectFlowDispatcher)
            .produceEvent()
            .distinctUntilChangedControlled(isEnableDistinctUntilChanged)
            .reduceEffect()
            .onStart {
                state = initialState
                emit(Notification.OnNext(initialState))
            }
            .distinctUntilChanged()
            .flowOn(stateFlowDispatcher)
            .share(shareScope)
            .trackSubscribers()

    /**
     * Always emits State values within collector's coroutine scope
     */
    override suspend fun collectSafely(collector: FlowCollector<Notification<State>>) {
        stateFlow.collect(collector::emit)
    }

    /**
     * Call this method to pass actions to the useCase
     * Doesn't require switching to particular thread/coroutine [thread safe]
     *
     * @throws IllegalStateException if feature doesn't have any subscribers
     */
    suspend fun accept(action: Action) {
        check(hasSubscribers) { "No observers to process action" }
        actionsFlow.emit(action)
    }

    suspend fun emitToActionCache(action: List<Action>) {
        actionCacheFlow.emit(action)
    }

    private fun observeBootstrap(): Flow<Notification<Effect>> = bootstrap
        .invoke()
        .materialize()
        .onEach { effect -> if (useExtendedLogs) effect.logValue(tag, "bootstrap") }


    private fun observeActions(): Flow<Notification<Effect>> = merge(
        actionsFlow,
        actionCacheFlow.flatMapConcat { it.asFlow() }
    ).flatMapMerge { action ->
        action.logValue(tag, "action")
        actor
            .invoke(state, action)
            .materialize()
            .onEach { effect -> if (useExtendedLogs) effect.logValue(tag, "effect") }
    }

    private fun Flow<Notification<Effect>>.produceEvent(): Flow<Notification<Effect>> = transform { effect ->
        if (effect is Notification.OnNext) {
            try {
                eventProducer
                    .invoke(effect.value)
                    ?.also { event ->
                        if (useExtendedLogs) event.logValue(tag, "event")
                        eventsFlow.emit(event)
                    }
            } catch (error: Throwable) {
                emit(Notification.OnError(error))
            }
        }
        emit(effect)
    }

    private fun Flow<Notification<Effect>>.reduceEffect(): Flow<Notification<State>> = map { effect ->
        when (effect) {
            is Notification.OnNext -> {
                runCatching {
                    val newState = reducer.invoke(state, effect.value)
                    state = newState
                    Notification.OnNext(state)
                }.getOrElse {
                    Notification.OnError(it)
                }
            }

            is Notification.OnError -> Notification.OnError(effect.error)
        }
    }

    private fun <T> Flow<T>.trackSubscribers(): Flow<T> {
        return onStart { subscribers.incrementAndGet() }
            .onCompletion { subscribers.decrementAndGet() }
    }
}
