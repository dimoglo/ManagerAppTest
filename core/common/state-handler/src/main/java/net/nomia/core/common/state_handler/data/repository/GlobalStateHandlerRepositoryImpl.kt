package net.nomia.core.common.state_handler.data.repository

import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.nomia.core.common.state_handler.di.GlobalStateHandlerModule.Companion.SharedPreferenceGlobalStateHandler
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import javax.inject.Inject
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
internal class GlobalStateHandlerRepositoryImpl @Inject constructor(
    @SharedPreferenceGlobalStateHandler private val sharedPreferences: SharedPreferences,
) : StateHandlerRepository {

    private val configsFlow = mutableMapOf<String, MutableStateFlow<Any?>>()

    private fun <T> setValueToConfigsFlow(key: String, value: T) {
        if (!configsFlow.containsKey(key)) {
            configsFlow[key] = MutableStateFlow(value)
        } else {
            configsFlow[key]?.value = value
        }
    }

    private fun <T> saveValueToSharedPreferences(key: String, value: T) {
        sharedPreferences.edit().apply {
            when (value) {
                is Int -> putInt(key, value)
                is Float -> putFloat(key, value)
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Boolean -> putBoolean(key, value)
            }
        }.apply()
    }

    private fun <T> getValueFromConfigsFlow(key: String): T? = configsFlow[key]?.value as T?

    private fun <T : Any> loadValueFromSharedPreferences(key: String, clazz: KClass<T>): T? =
        when (clazz) {
            Int::class -> sharedPreferences.getInt(key, 0) as T
            Float::class -> sharedPreferences.getFloat(key, 0f) as T
            Long::class -> sharedPreferences.getLong(key, 0L) as T
            String::class -> sharedPreferences.getString(key, "") as T
            Boolean::class -> sharedPreferences.getBoolean(key, false) as T
            else -> null
        }

    override fun <T> save(key: String, value: T) {
        setValueToConfigsFlow(key = key, value = value)
        saveValueToSharedPreferences(key = key, value = value)
    }

    override fun <T : Any> get(key: String, clazz: KClass<T>): T? =
        when {
            configsFlow.containsKey(key) -> getValueFromConfigsFlow(key)

            sharedPreferences.contains(key) -> loadValueFromSharedPreferences(
                key = key,
                clazz = clazz
            )?.also { value ->
                setValueToConfigsFlow(key, value)
            }

            else -> null
        }

    override fun <T : Any> observe(key: String, clazz: KClass<T>, initialValue: T?): StateFlow<T?> {
        if (!configsFlow.containsKey(key) && sharedPreferences.contains(key)) {
            loadValueFromSharedPreferences(key = key, clazz = clazz)?.let { value ->
                setValueToConfigsFlow(key, value)
            }
        }

        return configsFlow.getOrPut(key) { MutableStateFlow(initialValue) }.asStateFlow() as StateFlow<T>
    }
}
