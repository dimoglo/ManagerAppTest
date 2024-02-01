package net.nomia.pos.ui.auth.external.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import net.nomia.common.ui.composable.NomiaFilledButton
import net.nomia.common.ui.composable.NomiaOutlinedTextField
import net.nomia.common.ui.composable.NomiaSpinner
import net.nomia.common.ui.theme.spacers
import net.nomia.pos.R
import net.nomia.pos.core.text.Content
import net.nomia.pos.ui.auth.external.ExternalAuthViewModel.LoginCountdownState
import kotlin.time.Duration.Companion.minutes

@Composable
internal fun WelcomeForm(
    loginProvider: () -> TextFieldValue,
    loginCountdown: LoginCountdownState?,
    loading: Boolean,
    errorMessage: String?,
    onLoginInputChange: (TextFieldValue) -> Unit,
    onClearLoginInput: () -> Unit,
    onSignIn: (login: String) -> Unit,
) {
    val login = loginProvider()
    val resources = LocalContext.current.resources
    val displayedErrorContent by remember(errorMessage, login, loginCountdown) {
        mutableStateOf(
            when {
                loginCountdown?.login == login.text -> Content.ResValue(
                    stringRes = R.string.rate_limit_countdown,
                    args = listOf(
                        loginCountdown.timeLeft.inWholeMinutes,
                        loginCountdown.timeLeft.inWholeSeconds % SECONDS_IN_MINUTE
                    ),
                )
                errorMessage != null -> Content.Text(errorMessage)
                else -> Content.Empty
            }
        )
    }

    NomiaOutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = login,
        onValueChange = onLoginInputChange,
        onCancel = onClearLoginInput,
        enabled = !loading,
        label = { Text(text = stringResource(R.string.phone_or_email)) },
        isError = displayedErrorContent !is Content.Empty,
        supportingText = {
            with(displayedErrorContent) {
                val displayedErrorText = when (this) {
                    is Content.ResValue -> stringValue(resources)
                    is Content.Text -> text
                    else -> null
                }
                displayedErrorText?.let { Text(text = it) }
            }
        },
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.None,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Send
        ),
        keyboardActions = KeyboardActions(onSend = { onSignIn(login.text) }),
    )

    Spacer(modifier = Modifier.height(MaterialTheme.spacers.large))

    NomiaFilledButton(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onSignIn(login.text) },
        enabled = login.text.isNotEmpty() && loginCountdown?.login != login.text
    ) {
        if (loading) {
            NomiaSpinner()
        } else {
            Text(text = stringResource(id = R.string.login))
        }
    }
}

private val SECONDS_IN_MINUTE = 1.minutes.inWholeSeconds
