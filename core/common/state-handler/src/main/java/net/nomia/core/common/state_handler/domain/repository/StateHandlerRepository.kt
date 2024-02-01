package net.nomia.core.common.state_handler.domain.repository

import kotlinx.coroutines.flow.StateFlow
import kotlin.reflect.KClass

interface StateHandlerRepository {

    fun <T> save(key: String, value : T)
    fun <T : Any> get(key: String, clazz: KClass<T>): T?
    fun <T : Any> observe(key: String, clazz: KClass<T>, initialValue: T? = null): StateFlow<T?>
}
