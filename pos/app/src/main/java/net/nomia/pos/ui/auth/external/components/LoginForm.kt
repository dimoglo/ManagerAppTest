package net.nomia.pos.ui.auth.external.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import net.nomia.common.data.model.SignInResponse
import net.nomia.common.ui.composable.NomiaFilledButton
import net.nomia.common.ui.composable.NomiaOutlinedTextField
import net.nomia.common.ui.composable.NomiaSpinner
import net.nomia.common.ui.composable.defaultTrailingContent
import net.nomia.common.ui.extensions.clickableNoIndication
import net.nomia.common.ui.theme.spacers
import net.nomia.pos.R
import net.nomia.pos.ui.auth.common.CodeField
import net.nomia.pos.ui.auth.external.ExternalAuthViewModel
import net.nomia.pos.domain.auth.external.ExternalAuthUiState
import net.nomia.pos.domain.auth.internal.Code
import net.nomia.pos.domain.auth.internal.drawNumericPattern
import net.nomia.pos.ui.utils.stringValue

@Composable
internal fun LogInForm(
    loginState: ExternalAuthUiState.Login,
    loginProvider: () -> String,
    passwordProvider: () -> String,
    onPasswordInputChange: (String) -> Unit,
    codeProvider: () -> Code.Numeric,
    onCodeInputChange: (String) -> Unit,
    resendCodeProvider: () -> ExternalAuthViewModel.ResendCodeState,
    onLogIn: (signIn: SignInResponse, password: String) -> Unit,
    onResendCode: (signIn: SignInResponse, login: String) -> Unit,
    onAuthSuccessed: suspend CoroutineScope.() -> Unit,
) {
    val login = loginProvider()

    LaunchedEffect(Unit) { onAuthSuccessed() }

    when (loginState) {
        is ExternalAuthUiState.Login.Email, is ExternalAuthUiState.Login.Phone -> {
            ExternalAuthContent(
                isCenterAligned = true,
                title = stringResource(id = loginState.titleResId),
                subtitle = stringResource(id = loginState.subtitleResId, login)
            ) {
                LoginByCode(
                    codeProvider = codeProvider,
                    onCodeInputChange = onCodeInputChange,
                    resendCodeProvider = resendCodeProvider,
                    onResendCode = { onResendCode(loginState.signIn, login) },
                )
            }
        }

        is ExternalAuthUiState.Login.Password -> {
            ExternalAuthContent(
                title = stringResource(id = loginState.titleResId),
                subtitle = stringResource(id = loginState.subtitleResId, login)
            ) {
                LoginByPassword(
                    loading = loginState.loading,
                    errorMessage = loginState.errorMessage?.stringValue(),
                    passwordProvider = passwordProvider,
                    onPasswordInputChange = onPasswordInputChange,
                    logIn = { password -> onLogIn(loginState.signIn, password) }
                )
            }
        }
    }
}

@Composable
private fun LoginByPassword(
    loading: Boolean,
    errorMessage: String?,
    onPasswordInputChange: (String) -> Unit,
    passwordProvider: () -> String,
    logIn: (password: String) -> Unit,
) {
    val password = passwordProvider()
    val logInCallback = { logIn(password) }
    val isFocused = remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }
    val visualTransformation = remember(showPassword) {
        if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
    }

    NomiaOutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password,
        onValueChange = onPasswordInputChange,
        enabled = !loading,
        label = { Text(text = stringResource(R.string.hint_password)) },
        isError = errorMessage != null,
        isFocused = isFocused,
        trailingIcon = passwordTrailingIcon(
            isError = errorMessage != null,
            onChangeKeyboardOptions = { showPassword = !showPassword },
            value = password,
            enabled = !loading,
            isFocused = isFocused.value,
            showPassword = showPassword
        ),
        supportingText = { errorMessage?.let { Text(text = it) } },
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = { logInCallback() }),
        showKeyboard = true
    )

    Spacer(modifier = Modifier.height(40.dp))

    NomiaFilledButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = logInCallback,
        enabled = password.isNotEmpty()
    ) {
        if (loading) {
            NomiaSpinner()
        } else {
            Text(text = stringResource(id = R.string.continue_action))
        }
    }
}

@Composable
private fun passwordTrailingIcon(
    isError: Boolean,
    onChangeKeyboardOptions: () -> Unit,
    value: String,
    enabled: Boolean,
    isFocused: Boolean,
    showPassword: Boolean,
): @Composable (() -> Unit)? =
    defaultTrailingContent(
        isError = isError,
        onClick = onChangeKeyboardOptions,
        value = value,
        enabled = enabled,
        isFocused = isFocused,
        trailingIcon = {
            IconButton(onClick = onChangeKeyboardOptions) {
                Icon(
                    painter = painterResource(
                        id = if (showPassword)
                            R.drawable.ic_visibility_off_24
                        else
                            R.drawable.ic_visibility_on_24
                    ),
                    contentDescription = null
                )
            }
        }
    )

@OptIn(ExperimentalTextApi::class)
@Composable
private fun LoginByCode(
    codeProvider: () -> Code.Numeric,
    onCodeInputChange: (String) -> Unit,
    resendCodeProvider: () -> ExternalAuthViewModel.ResendCodeState,
    onResendCode: () -> Unit,
) {
    val code = codeProvider()
    val resendCode = resendCodeProvider()
    val colorScheme = MaterialTheme.colorScheme
    val textStyle = MaterialTheme.typography.titleLarge
    val textMeasure = rememberTextMeasurer()
    val configuration = remember(colorScheme, textStyle, textMeasure) {
        Code.Configuration.Numeric(
            textMeasurer = textMeasure,
            textStyle = textStyle,
            colorScheme = Code.Configuration.ColorScheme(
                filled = colorScheme.onSurface,
                empty = colorScheme.onSurfaceVariant,
                error = colorScheme.error
            )
        )
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacers.extraLarge))

    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
        NomiaOutlinedTextField(
            modifier = Modifier
                .width(1.dp)
                .clickableNoIndication { } // prevent cursor appearing on click
                .alpha(0F), // invisible component
            value = code.input,
            onValueChange = onCodeInputChange,
            trailingIcon = null,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            showKeyboard = true
        )
        CodeField(size = ExternalAuthUiState.Login.CodeSize) { position ->
            drawNumericPattern(
                position = position,
                value = code,
                configurationProvider = { configuration },
            )
        }
    }

    Spacer(modifier = Modifier.height(MaterialTheme.spacers.extraLarge))

    NomiaFilledButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = onResendCode,
        enabled = resendCode is ExternalAuthViewModel.ResendCodeState.Empty
    ) {
        when (resendCode) {
            is ExternalAuthViewModel.ResendCodeState.CountDown -> {
                Text(
                    text = stringResource(
                        id = R.string.retry_code_with_timer,
                        DateUtils.formatElapsedTime(resendCode.count.toLong())
                    )
                )
            }

            ExternalAuthViewModel.ResendCodeState.Empty -> {
                Text(text = stringResource(id = R.string.retry_code))
            }

            is ExternalAuthViewModel.ResendCodeState.StartCounter -> {}
        }
    }
}
