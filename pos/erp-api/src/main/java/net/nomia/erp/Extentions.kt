package net.nomia.erp

import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloSubscriptionCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import net.nomia.erp.api.exception.NomiaApiResponseException
import net.nomia.erp.api.exception.NomiaNoMenusException
import net.nomia.erp.api.exception.NomiaPaymentRequiredException
import net.nomia.erp.api.exception.NomiaRateLimitException
import net.nomia.erp.api.exception.NomiaUnknownUserException
import timber.log.Timber
import java.math.BigDecimal

@ExperimentalCoroutinesApi
fun <T> ApolloCall<T>.apiFlow(): Flow<Response<T>> {
    return this.toFlow().onEach { response: Response<T> ->
        if (response.hasErrors()) {
            val errorMessage = response.errors?.joinToString { error -> error.message }
            try {
                response.errors?.forEach { error ->
                    val extensions = error.customAttributes["extensions"] as Map<*, *>?
                    if (extensions != null) {
                        when (extensions["type"] as String?) {
                            "IdentityNotRegistered" -> throw NomiaUnknownUserException(errorMessage)
                            "AccountHasNoMenus" -> throw NomiaNoMenusException(errorMessage)
                            "PaymentRequiredException" -> throw NomiaPaymentRequiredException(errorMessage)
                            "RateLimitException" -> {
                                val timeLeft = extensions["elapsedTime"] as BigDecimal?
                                throw NomiaRateLimitException(errorMessage, timeLeft)
                            }
                        }
                    }
                }
            } catch (e: RuntimeException) {
                Timber.e(e)
                throw e
            }
            val apiResponseException = NomiaApiResponseException(errorMessage)
            Timber.e(apiResponseException)
            throw apiResponseException
        }
    }
}

@ExperimentalCoroutinesApi
fun <T> ApolloSubscriptionCall<T>.apiFlow(): Flow<Response<T>> {
    return this.toFlow().onEach { response: Response<T> ->
        if (response.hasErrors()) {
            throw NomiaApiResponseException(response.errors?.joinToString { error -> error.message })
        }
    }
}
