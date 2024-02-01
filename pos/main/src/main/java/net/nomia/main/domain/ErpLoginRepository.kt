package net.nomia.main.domain

import com.apollographql.apollo.request.RequestHeaders
import com.auth0.android.jwt.JWT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transformLatest
import net.nomia.common.data.model.LostPasswordResponse
import net.nomia.common.data.model.SignInResponse
import net.nomia.common.data.model.Terminal
import net.nomia.erp.api.ErpClientService
import net.nomia.erp.api.exception.NomiaNoMenusException
import net.nomia.erp.api.exception.NomiaRateLimitException
import net.nomia.erp.api.exception.NomiaUnknownUserException
import net.nomia.erp.apiFlow
import net.nomia.erp.mutation.CreateApplicationTokenMutation
import net.nomia.erp.mutation.LogInByCodeMutation
import net.nomia.erp.mutation.LogInByPasswordMutation
import net.nomia.erp.mutation.LostPasswordMutation
import net.nomia.erp.mutation.RefreshTokenMutation
import net.nomia.erp.mutation.SignInPosMutation
import net.nomia.main.domain.model.Auth
import net.nomia.main.exception.AuthException
import net.nomia.main.exception.NoMenusException
import net.nomia.main.exception.RateLimitException
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.exception.NetworkException
import net.nomia.pos.core.exception.NomiaException
import net.nomia.pos.core.text.Content
import net.nomia.settings.domain.model.ApplicationToken
import timber.log.Timber
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.toKotlinDuration
import net.nomia.common.ui.R as CommonRes

@Singleton
class ErpLoginRepository @Inject constructor(
    private val erpClientService: ErpClientService,
) {

    fun signIn(login: String): Flow<Response<out SignInResponse>> =
        erpClientService.principalClient()
            .take(1)
            .transformLatest { client ->
                emit(Response.Loading)
                client.mutate(SignInPosMutation(login))
                    .toBuilder()
                    .requestHeaders(defaultHeaders)
                    .build()
                    .apiFlow()
                    .catch { e ->
                        val errorMessage = e.message?.let { Content.Text(it) }
                            ?: Content.ResValue(CommonRes.string.unknown_error)
                        when (e) {
                            !is NomiaException ->
                                emit(Response.Error(
                                    message = errorMessage,
                                    throwable = NetworkException(e)
                                ))
                            is NomiaUnknownUserException ->
                                emit(Response.Error(
                                    message = errorMessage,
                                    throwable = AuthException(e)
                                ))
                            is NomiaNoMenusException ->
                                emit(Response.Error(
                                    message = errorMessage,
                                    throwable = NoMenusException(e)
                                ))
                            is NomiaRateLimitException -> {
                                emit(Response.Error(
                                    message = errorMessage,
                                    throwable = RateLimitException(
                                        cause = e,
                                        timeLeft = e.timeLeft
                                    )
                                ))
                            }
                            else ->
                                emit(Response.Error(
                                    message = errorMessage,
                                    throwable = e
                                ))
                        }
                    }
                    .mapLatest { it.data!!.signInV2?.toDomain() }
                    .collectLatest { result ->
                        if (result == null)
                            emit(Response.Error(message = Content.ResValue(CommonRes.string.unknown_error)))
                        else
                            emit(Response.Success(result))
                    }
            }
            .flowOn(Dispatchers.IO)


    fun login(signIn: SignInResponse, password: String): Flow<Auth> {
        return with(signIn) {
            if (hasPassword) {
                loginByPassword(requestId, password)
            } else {
                loginByCode(requestId, password)
            }
        }.catch { e ->
            throw if (e !is NomiaException) {
                NetworkException(e)
            } else {
                e
            }
        }
    }

    fun restorePassword(login: String): Flow<LostPasswordResponse> {
        return erpClientService.principalClient().take(1).flatMapLatest { client ->
            client.mutate(LostPasswordMutation(login)).apiFlow()
        }.mapLatest { it.data!!.lostPassword.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    private fun loginByCode(requestId: SignInResponse.RequestId, code: String): Flow<Auth> {
        return erpClientService.principalClient().take(1).flatMapLatest { client ->
            client.mutate(LogInByCodeMutation(requestId.value, code)).apiFlow()
        }.mapLatest { it.data!!.auth.fragments.authFragment.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    private fun loginByPassword(requestId: SignInResponse.RequestId, password: String): Flow<Auth> {
        return erpClientService.principalClient().take(1).flatMapLatest { client ->
            client.mutate(LogInByPasswordMutation(requestId.value, password)).apiFlow()
        }.mapLatest { it.data!!.auth.fragments.authFragment.toDomain() }
            .flowOn(Dispatchers.IO)
    }

    fun watchRefreshToken(auth: Auth) = flow {
        var latestAuth = auth
        emit(latestAuth)
        while (latestAuth.accessToken.expiresAt != null) {
            erpClientService.principalClient()
                .take(1)
                .flatMapLatest { client ->
                    client.mutate(RefreshTokenMutation(refreshToken = latestAuth.refreshToken.toString()))
                        .apiFlow()
                }.mapLatest { it.data!!.refreshTokens.fragments.authFragment.toDomain() }
                .catch { e -> Timber.tag(TAG).w(e, "watchRefreshToken") }
                .collect { response ->
                    latestAuth = response
                    emit(response)
                }

            val duration = Duration.between(
                latestAuth.accessToken.issuedAt?.toInstant(),
                latestAuth.accessToken.expiresAt?.toInstant()?.plusSeconds(60)
            ).toKotlinDuration()

            delay(duration)
        }
    }

    fun createApplicationToken(auth: Auth, terminal: Terminal): Flow<ApplicationToken> {
        return erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = terminal.organization.id
        ).take(1).flatMapLatest { client ->
            client.mutate(
                CreateApplicationTokenMutation(
                    name = terminal.name,
                    storeId = terminal.storeId.value,
                    terminalId = terminal.id.value
                )
            ).apiFlow()
        }.mapLatest { ApplicationToken(accessToken = JWT(it.data!!.createApplicationToken)) }
            .flowOn(Dispatchers.IO)
    }

    companion object {
        private const val TAG = "ErpLoginRepository"

        private val defaultHeaders = RequestHeaders.Builder()
            .addHeader("Accept-Language", "ru-RU")
            .build()
    }
}
