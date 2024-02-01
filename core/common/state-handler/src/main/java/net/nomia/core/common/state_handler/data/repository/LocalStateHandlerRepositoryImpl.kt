package net.nomia.core.common.state_handler.data.repository

import androidx.lifecycle.SavedStateHandle
import kotlinx.coroutines.flow.StateFlow
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import javax.inject.Inject
import kotlin.reflect.KClass

internal class LocalStateHandlerRepositoryImpl @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) : StateHandlerRepository {

    override fun <T> save(key: String, value: T) {
        savedStateHandle[key] = value
    }

    override fun <T : Any> get(
        key: String,
        clazz: KClass<T>,
    ): T? = savedStateHandle.get<T>(key)

    override fun <T : Any> observe(
        key: String,
        clazz: KClass<T>,
        initialValue: T?,
    ): StateFlow<T?> = savedStateHandle.getStateFlow(key, initialValue)
}
