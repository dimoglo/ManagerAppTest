package net.nomia.pos.ui.auth.external

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.platform.UriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import net.nomia.common.data.model.SignInResponse
import net.nomia.common.ui.composable.NomiaBasicScrollableScaffold
import net.nomia.common.ui.previews.ThemePreviews
import net.nomia.common.ui.theme.NomiaThemeMaterial3
import net.nomia.pos.R
import net.nomia.pos.ui.navigation.PosSetupDestination
import net.nomia.pos.ui.auth.common.BaseAuthLayout
import net.nomia.pos.ui.auth.external.ExternalAuthViewModel.LoginCountdownState
import net.nomia.pos.ui.auth.external.components.ExternalActionForm
import net.nomia.pos.ui.auth.external.components.ExternalAuthContent
import net.nomia.pos.ui.auth.external.components.LogInForm
import net.nomia.pos.ui.auth.external.components.WelcomeForm
import net.nomia.pos.domain.auth.external.ExternalAuthUiState
import net.nomia.pos.domain.auth.internal.Code
import net.nomia.pos.ui.utils.stringValue
import net.nomia.pos.ui.navigation.getDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExternalAuthScreen(
    navController: NavHostController,
    viewModel: ExternalAuthViewModel = hiltViewModel(),
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val uiState by viewModel.externalAuthState.collectAsState()

    NomiaBasicScrollableScaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        navigationIcon = {
            if (uiState !is ExternalAuthUiState.Welcome) {
                IconButton(onClick = viewModel::navigateToWelcome) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back_24),
                        contentDescription = null,
                    )
                }
            }
        },
        topBarColors = topAppBarColors(
            containerColor = Color.Transparent,
            scrolledContainerColor = Color.Transparent
        ),
    ) { contentPadding ->
        BaseAuthLayout(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(top = contentPadding.calculateTopPadding()),
        ) {
            val resendCode by viewModel.resendCodeState.collectAsState(
                initial = ExternalAuthViewModel.ResendCodeState.Empty
            )
            val loginCountdown by viewModel.loginCountdownState.collectAsState()
            val login by viewModel.inputLoginState.collectAsState()
            val password by viewModel.inputPasswordState.collectAsState()
            val code by viewModel.inputCodeState.collectAsState()
            val activity = LocalContext.current as? Activity
            val defaultErrorMessageLiteral = stringResource(id = R.string.network_unavailable)

            LaunchedEffect(Unit) {
                viewModel.loginErrorMessageState.collectLatest { message ->
                    snackbarHostState.showSnackbar(message = message ?: defaultErrorMessageLiteral)
                }
            }

            BackHandler {
                when (uiState) {
                    is ExternalAuthUiState.Welcome -> activity?.finish()
                    else -> viewModel.navigateToWelcome()
                }
            }

            ExternalAuthLayout(
                uiState = uiState,
                resendCodeProvider = { resendCode },
                loginCountdown = loginCountdown,
                onLoginInputChange = viewModel::onLoginInputChange,
                onClearLoginInput = viewModel::onClearLoginInput,
                loginProvider = { login },
                passwordProvider = { password },
                onPasswordInputChange = viewModel::onPasswordInputChange,
                codeProvider = { code },
                onCodeInputChange = viewModel::onCodeInputChange,
                onSignIn = viewModel::actionSignIn,
                onNavigateToErp = viewModel::navigateToErp,
                onUrlNavigation = { uriHandler ->
                    viewModel.navigateToExternalUrl.onEach(uriHandler::openUri)
                        .launchIn(this)
                },
                onLogIn = viewModel::actionLogin,
                onAuthSuccessed = {
                    viewModel.authSuccessEvent.filter { it }
                        .onEach {
                            navController.popBackStack()
                            navController.navigate(PosSetupDestination.getDestination())
                        }
                        .launchIn(this)
                },
                onResendCode = viewModel::resendCode,
            )
        }
    }
}

@Composable
private fun ExternalAuthLayout(
    uiState: ExternalAuthUiState,
    resendCodeProvider: () -> ExternalAuthViewModel.ResendCodeState,
    loginCountdown: LoginCountdownState?,
    onLoginInputChange: (TextFieldValue) -> Unit,
    onClearLoginInput: () -> Unit,
    loginProvider: () -> TextFieldValue,
    passwordProvider: () -> String,
    onPasswordInputChange: (String) -> Unit,
    codeProvider: () -> Code.Numeric,
    onCodeInputChange: (String) -> Unit,
    onSignIn: (login: String) -> Unit,
    onNavigateToErp: () -> Unit,
    onLogIn: (signIn: SignInResponse, password: String) -> Unit,
    onAuthSuccessed: suspend CoroutineScope.() -> Unit,
    onResendCode: (signIn: SignInResponse, login: String) -> Unit,
    onUrlNavigation: suspend CoroutineScope.(UriHandler) -> Unit,
) {
    val uriHandler = LocalUriHandler.current

    LaunchedEffect(Unit) { onUrlNavigation(uriHandler) }

    when (uiState) {
        is ExternalAuthUiState.Welcome -> {
            ExternalAuthContent(
                title = stringResource(id = uiState.titleResId),
                subtitle = stringResource(id = uiState.subtitleResId)
            ) {
                WelcomeForm(
                    loginProvider = loginProvider,
                    loginCountdown = loginCountdown,
                    loading = uiState.loading,
                    errorMessage = uiState.errorMessage?.stringValue(),
                    onLoginInputChange = onLoginInputChange,
                    onClearLoginInput = onClearLoginInput,
                    onSignIn = onSignIn,
                )
            }
        }

        is ExternalAuthUiState.Login -> LogInForm(
            loginState = uiState,
            loginProvider = { loginProvider.invoke().text },
            passwordProvider = passwordProvider,
            onPasswordInputChange = onPasswordInputChange,
            codeProvider = codeProvider,
            onCodeInputChange = onCodeInputChange,
            resendCodeProvider = resendCodeProvider,
            onLogIn = onLogIn,
            onResendCode = onResendCode,
            onAuthSuccessed = onAuthSuccessed,
        )

        is ExternalAuthUiState.ExternalActionRequired -> {
            ExternalAuthContent(
                title = stringResource(id = uiState.titleResId),
                subtitle = stringResource(id = uiState.subtitleResId)
            ) {
                ExternalActionForm(
                    buttonTitleResId = uiState.buttonTitleResId,
                    onClickAction = onNavigateToErp
                )
            }
        }
    }
}

@ThemePreviews
@Composable
fun ExternalAuthScreenPreview() {
    NomiaThemeMaterial3 {
        Surface {
            BaseAuthLayout {
                ExternalAuthLayout(
                    uiState = ExternalAuthUiState.Welcome(),
                    resendCodeProvider = { ExternalAuthViewModel.ResendCodeState.Empty },
                    loginCountdown = null,
                    onSignIn = {},
                    onNavigateToErp = { },
                    onLogIn = { _, _ -> },
                    onAuthSuccessed = {},
                    onResendCode = { _, _ -> },
                    onUrlNavigation = {},
                    onLoginInputChange = {},
                    onClearLoginInput = {},
                    loginProvider = { TextFieldValue() },
                    onPasswordInputChange = {},
                    passwordProvider = { "" },
                    codeProvider = { Code.Numeric() },
                    onCodeInputChange = {},
                )
            }
        }
    }
}
