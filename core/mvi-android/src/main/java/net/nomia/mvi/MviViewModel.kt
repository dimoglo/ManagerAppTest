package net.nomia.mvi

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import net.nomia.mvi.utils.Notification
import net.nomia.mvi.utils.dematerialize
import androidx.compose.runtime.State as AndroidState

abstract class MviViewModel<State : Any, Action : Any>(
    factory: MviFeatureFactory<Action, *, State, Event>
) : ViewModel() {

    companion object{
        private const val EVENTS_BUFFER_SIZE = 10
    }

    private val feature = factory.create(viewModelScope)

    private var featureStateFlow: StateFlow<State>? = null

    private var featureEventsFlow = MutableSharedFlow<Event?>(replay = 0, EVENTS_BUFFER_SIZE, BufferOverflow.SUSPEND)

    @Composable
    fun collectAsState(
        launchActions: List<Action> = listOf(),
        onEvent: suspend CoroutineScope.(Event) -> Unit = {}
    ) : AndroidState<State>{
        val state = getStateFlow(launchActions).collectAsState(feature.state)
        LaunchedEffect(Unit) {
            featureEventsFlow.collectLatest { event ->
                event?.let { onEvent(it) }
            }
        }
        return state
    }

    fun acceptAction(action: Action) {
        viewModelScope.launch { feature.accept(action) }
    }

    private fun <State> Flow<*>.processFeature(
        eventFlow: MutableSharedFlow<Event?>,
        errorEventProducer: (Throwable) -> Event?
    ) = transform<Any?, Notification<State>> {
        when (it) {
            is Event -> flatEvent(it, errorEventProducer).forEach { event -> eventFlow.tryEmit(event) }
            is Notification.OnError<*> -> errorEventProducer(it.error)?.let { event -> eventFlow.tryEmit(event) }
            is Notification.OnNext<*> -> emit(it as Notification.OnNext<State>)
        }
    }.dematerialize()

    private fun flatEvent(event: Event, errorEventProducer: (Throwable) -> Event?): List<Event> {
        return when (event) {
            is CompositeEvent -> event.events.flatMap { flatEvent(it, errorEventProducer) }
            is ErrorEvent -> listOfNotNull(errorEventProducer(event.throwable))
            else -> listOf(event)
        }
    }

    private fun getStateFlow(launchActions: List<Action> = listOf()): SharedFlow<State> {
        return featureStateFlow.takeIf { it != null } ?: let {
            merge(feature, feature.events)
                .processFeature<State>(featureEventsFlow, ::produceErrorEvent)
                .stateIn(viewModelScope, SharingStarted.Lazily, feature.state)
                .also { featureStateFlow = it }
                .onSubscription {
                    viewModelScope.launch {
                        feature.emitToActionCache(launchActions)
                    }
                }
        }
    }

    private fun produceErrorEvent(error: Throwable): Event {
        // TODO implement default behaviour or override
        return ErrorEvent(error)
    }
}
