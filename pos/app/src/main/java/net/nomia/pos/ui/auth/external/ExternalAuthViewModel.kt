package net.nomia.pos.ui.auth.external

import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import net.nomia.common.data.Constants.SharingStartedWithDefaultTimeout
import net.nomia.common.data.model.IdentityType
import net.nomia.common.data.model.SignInResponse
import net.nomia.common.ui.extensions.replacePrefix
import net.nomia.common.ui.extensions.trim
import net.nomia.main.domain.ErpLoginRepository
import net.nomia.main.domain.PrincipalRepository
import net.nomia.main.exception.AuthException
import net.nomia.main.exception.NoMenusException
import net.nomia.main.exception.RateLimitException
import net.nomia.pos.R
import net.nomia.pos.core.data.Constraints.MAX_LOGIN_LENGTH
import net.nomia.pos.core.data.Constraints.MAX_PASSWORD_LENGTH
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.exception.NetworkException
import net.nomia.pos.core.text.Content
import net.nomia.pos.domain.auth.external.ExternalAuthUiState
import net.nomia.pos.domain.auth.internal.Code
import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.ServerProvider
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.Duration.Companion.seconds
import net.nomia.common.ui.R as CommonRes

@HiltViewModel
class ExternalAuthViewModel @Inject constructor(
    private val erpLoginRepository: ErpLoginRepository,
    private val principalRepository: PrincipalRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _externalAuthState = MutableStateFlow<ExternalAuthUiState>(ExternalAuthUiState.Welcome())
    val externalAuthState = _externalAuthState.asStateFlow()

    private val _inputLoginState = MutableStateFlow(TextFieldValue())
    val inputLoginState = _inputLoginState

    private val _inputPasswordState = MutableStateFlow("")
    val inputPasswordState = _inputPasswordState.asStateFlow()

    private val _inputCodeState = MutableStateFlow(Code.Numeric())
    val inputCodeState = _inputCodeState.asStateFlow()

    private val _loginErrorMessageState = MutableSharedFlow<String?>()
    val loginErrorMessageState = _loginErrorMessageState.asSharedFlow()

    private val _navigateToExternalUrl = MutableSharedFlow<String>()
    val navigateToExternalUrl = _navigateToExternalUrl.asSharedFlow()

    private val _authSuccessEvent = MutableSharedFlow<Boolean>()
    val authSuccessEvent = _authSuccessEvent.asSharedFlow()

    val serverProvider = settingsRepository.getServerProvider()
        .stateIn(viewModelScope, SharingStartedWithDefaultTimeout, ServerProvider.DEFAULT)

    private val _loginCountdownState = MutableStateFlow<LoginCountdownState?>(null)
    val loginCountdownState = _loginCountdownState
        .filterNotNull()
        .transformLatest { initialState ->
            (initialState.timeLeft.inWholeSeconds downTo 0).forEach { second ->
                if (second == 0L) {
                    emit(null)
                } else {
                    emit(
                        LoginCountdownState(
                        timeLeft = second.seconds,
                        login = initialState.login
                    )
                    )
                    delay(1.seconds)
                }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), null)

    private val _resendCodeState = MutableStateFlow<ResendCodeState>(ResendCodeState.Empty)
    internal val resendCodeState = _resendCodeState.transformLatest { value ->
        when (value) {
            is ResendCodeState.StartCounter -> {
                (60 downTo 1).forEach { second ->
                    emit(
                        ResendCodeState.CountDown(
                            login = value.login,
                            signInResponse = value.signInResponse,
                            count = second
                        )
                    )
                    delay(1.seconds)
                }
                emit(ResendCodeState.Empty)
            }

            else -> emit(value)
        }
    }.shareIn(viewModelScope, SharingStarted.WhileSubscribed(), replay = 1)

    init {
        _inputLoginState.onEach {
            val authState = externalAuthState.value

            if (authState is ExternalAuthUiState.Welcome) {
                if (authState.errorMessage != null) {
                    _externalAuthState.update { authState.copy(errorMessage = null) }
                }
            }
        }.launchIn(viewModelScope)

        _inputPasswordState.onEach {
            val authState = externalAuthState.value

            if (authState is ExternalAuthUiState.Login) {
                if (authState.errorMessage != null) {
                    _externalAuthState.update { authState.signIn.defineLoginType() }
                }
            }
        }.launchIn(viewModelScope)

        _inputCodeState.filter { it.input.length == ExternalAuthUiState.Login.CodeSize }
            .mapLatest { it.input }
            .flatMapLatest { code ->
                erpLoginRepository.login(
                    signIn = (externalAuthState.value as ExternalAuthUiState.Login).signIn,
                    password = code
                )
            }
            .retryWhen {throwable, _ ->
                _inputCodeState.update { it.copy(isCorrect = false) }
                _loginErrorMessageState.emit(throwable.message)
                delay(1000L)
                _inputCodeState.update { Code.Numeric() }
                true
            }
            .onEach { auth ->
                principalRepository.login(auth)
                _authSuccessEvent.emit(true)
            }
            .launchIn(viewModelScope)
    }

    fun navigateToWelcome() {
        _externalAuthState.value = ExternalAuthUiState.Welcome()
    }

    fun onLoginInputChange(newLogin: TextFieldValue) {
        val newLoginFormatted = newLogin.trim(' ').replacePrefix("8", "+7")
        if (newLoginFormatted.text.length <= MAX_LOGIN_LENGTH) {
            _inputLoginState.update { newLoginFormatted }
        }
    }


    fun onClearLoginInput() =
        _inputLoginState.update { it.copy("", TextRange.Zero) }

    fun onPasswordInputChange(newPassword: String) {
        if (newPassword.length <= MAX_PASSWORD_LENGTH) {
            _inputPasswordState.update { newPassword }
        }
    }

    fun onCodeInputChange(newCode: String) {
        if (newCode.length <= ExternalAuthUiState.Login.CodeSize) {
            _inputCodeState.update { it.copy(input = newCode) }
        }
    }


    fun actionSignIn(login: String) = viewModelScope.launch {
        if (login.isPhoneNumber()) {
            processActionSignIn(login.replaceFirst("8", "+7"))
        } else {
            processActionSignIn(login)
        }
    }

    private fun String.isPhoneNumber(): Boolean = this.first() == '8'

    private suspend fun processActionSignIn(login: String) {
        _externalAuthState.value = ExternalAuthUiState.Welcome(loading = true)
        val resendCodeState = resendCodeState.first()
        if (resendCodeState is ResendCodeState.CountDown && resendCodeState.login == login) {
            _externalAuthState.value = resendCodeState.signInResponse.defineLoginType()
        } else {
            erpLoginRepository.signIn(login.trim())
                .collect { response ->
                    when (response) {
                        is Response.Error -> {
                            _externalAuthState.value = when (response.throwable) {
                                is AuthException -> {
                                    ExternalAuthUiState.ExternalActionRequired(
                                        titleResId = R.string.user_unregistered,
                                        subtitleResId = R.string.auth_action,
                                        buttonTitleResId = R.string.continue_in_browser,
                                    )
                                }
                                is NoMenusException -> {
                                    ExternalAuthUiState.ExternalActionRequired(
                                        titleResId = R.string.no_menus_created_title,
                                        subtitleResId = R.string.no_menus_created_subtitle,
                                        buttonTitleResId = R.string.no_menus_create,
                                    )
                                }
                                is RateLimitException -> {
                                    val throwable = response.throwable
                                    if (throwable is RateLimitException) {
                                        throwable.timeLeft?.longValueExact()?.nanoseconds?.let { timeLeft ->
                                            _loginCountdownState
                                                .emit(LoginCountdownState(timeLeft = timeLeft, login = login))
                                        }
                                    }
                                    ExternalAuthUiState.Welcome()
                                }
                                else -> {
                                    val errorMessage = if (response.throwable is NetworkException) {
                                        Content.ResValue(CommonRes.string.no_network)
                                    } else {
                                        response.message
                                    }
                                    ExternalAuthUiState.Welcome(errorMessage = errorMessage)
                                }
                            }
                            _resendCodeState.value = ResendCodeState.Empty
                        }

                        is Response.Success -> {
                            val signInResponse = response.result
                            _externalAuthState.value = signInResponse.defineLoginType()
                            _resendCodeState.value = ResendCodeState.StartCounter(
                                login = login,
                                signInResponse = signInResponse
                            )
                        }

                        else -> {}
                    }
                }
        }
    }

    private fun SignInResponse.defineLoginType(
        loading: Boolean = false,
        errorMessage: Content? = null,
    ) =
        when {
            hasPassword -> ExternalAuthUiState.Login.Password(
                signIn = this,
                loading = loading,
                errorMessage = errorMessage,
            )

            authType == IdentityType.Email -> ExternalAuthUiState.Login.Email(
                signIn = this,
                loading = loading,
                errorMessage = errorMessage,
            )

            else -> ExternalAuthUiState.Login.Phone(
                signIn = this,
                loading = loading,
                errorMessage = errorMessage,
            )
        }

    fun actionLogin(signIn: SignInResponse, password: String) {
        viewModelScope.launch {
            _externalAuthState.value = signIn.defineLoginType(loading = true)
            erpLoginRepository.login(signIn, password)
                .catch { e ->
                    val errorMessage = if (e is NetworkException) {
                        Content.ResValue(CommonRes.string.no_network)
                    } else {
                        e.message?.let { Content.Text(it) }
                    }
                    _externalAuthState.value = signIn.defineLoginType(errorMessage = errorMessage)
                }
                .collect { auth ->
                    principalRepository.login(auth)
                    _authSuccessEvent.emit(true)
                }
        }
    }

    fun navigateToErp() = viewModelScope.launch {
        _navigateToExternalUrl.emit(serverProvider.first().url)
    }

    fun resendCode(signIn: SignInResponse, login: String) = viewModelScope.launch {
        val currentState = externalAuthState.value
        if (currentState is ExternalAuthUiState.Login) {
            _externalAuthState.value = signIn.defineLoginType(loading = true)
        }

        erpLoginRepository.signIn(login)
            .collect { response ->
                when (response) {
                    is Response.Error -> {
                        _externalAuthState.value = if (response.throwable is AuthException) {
                            ExternalAuthUiState.ExternalActionRequired(
                                titleResId = R.string.user_unregistered,
                                subtitleResId = R.string.auth_action,
                                buttonTitleResId = R.string.continue_in_browser,
                            )
                        } else {
                            val errorMessage = if (response.throwable is NetworkException) {
                                Content.ResValue(CommonRes.string.no_network)
                            } else {
                                response.message
                            }

                            signIn.defineLoginType(errorMessage = errorMessage)
                        }
                        _resendCodeState.value = ResendCodeState.Empty
                    }

                    is Response.Success -> {
                        val signInResponse = response.result
                        _externalAuthState.value = signInResponse.defineLoginType()
                        _resendCodeState.value = ResendCodeState.StartCounter(
                            login = login,
                            signInResponse = signInResponse
                        )
                    }

                    else -> {}
                }
            }
    }

    internal sealed class ResendCodeState {
        object Empty : ResendCodeState()
        data class StartCounter(
            val login: String,
            val signInResponse: SignInResponse,
        ) : ResendCodeState()

        data class CountDown(
            val login: String,
            val signInResponse: SignInResponse,
            val count: Int,
        ) : ResendCodeState()
    }

    data class LoginCountdownState(
        val timeLeft: Duration,
        val login: String,
    )
}
