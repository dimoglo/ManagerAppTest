package net.nomia.pos.ui.auth.internal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import net.nomia.common.ui.composable.LogoutDialog
import net.nomia.common.ui.composable.NomiaButtonDefaults
import net.nomia.common.ui.composable.NomiaFilledIconButton
import net.nomia.common.ui.previews.ThemePreviews
import net.nomia.common.ui.theme.NomiaThemeMaterial3
import net.nomia.common.ui.theme.appResources
import net.nomia.common.ui.theme.paddings
import net.nomia.common.ui.theme.spacers
import net.nomia.pos.R
import net.nomia.pos.ui.auth.common.BaseAuthLayout
import net.nomia.pos.ui.auth.common.CodeField
import net.nomia.pos.domain.auth.internal.Code
import net.nomia.pos.domain.auth.internal.KeyboardItem
import net.nomia.pos.domain.auth.internal.drawPinPattern


@Composable
internal fun InternalAuthScreen(
    viewModel: InternalAuthViewModel = hiltViewModel()
) {

    BaseAuthLayout(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(top = MaterialTheme.paddings.xxLarge),
    ) {
        val pinState by viewModel.pinState.collectAsState()

        InternalAuthLayout(
            onDigit = viewModel::onDigit,
            onErase = viewModel::onErase,
            onLogout = viewModel::onLogout,
            pinProvider = { pinState }
        )
    }
}

@Composable
private fun InternalAuthLayout(
    onDigit: (String) -> Unit,
    onErase: () -> Unit,
    onLogout: () -> Unit,
    pinProvider: () -> Code.Pin,
) {
    var showLogoutDialog by remember { mutableStateOf(false) }
    val colorScheme = MaterialTheme.colorScheme
    val configuration = remember(colorScheme) {
        Code.Configuration.Pin(
            colorScheme = Code.Configuration.ColorScheme(
                filled = colorScheme.primary,
                empty = colorScheme.onSurfaceVariant,
                error = colorScheme.error
            )
        )
    }

    if (showLogoutDialog) {
        LogoutDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = onLogout
        )
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(MaterialTheme.appResources.textLogoResId),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Spacer(Modifier.height(40.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.internal_auth_enter_pin),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MaterialTheme.spacers.extraLarge))

        CodeField(size = InternalAuthViewModel.PinSize) { position ->
            drawPinPattern(
                position = position,
                value = pinProvider(),
                ConfigurationProvider = { configuration }
            )
        }
    }

    Spacer(Modifier.height(88.dp))

    KeyboardLayout(
        onDigit = onDigit,
        onErase = onErase,
        onLogout = { showLogoutDialog = true }
    )
}

@Composable
private fun KeyboardLayout(
    onDigit: (String) -> Unit,
    onErase: () -> Unit,
    onLogout: () -> Unit,
) {
    val keyboardElements = remember {
        listOf(
            KeyboardItem.Digit.One,
            KeyboardItem.Digit.Two,
            KeyboardItem.Digit.Three,

            KeyboardItem.Digit.Four,
            KeyboardItem.Digit.Five,
            KeyboardItem.Digit.Six,

            KeyboardItem.Digit.Seven,
            KeyboardItem.Digit.Eight,
            KeyboardItem.Digit.Nine,

            KeyboardItem.LogOut,
            KeyboardItem.Digit.Zero,
            KeyboardItem.Erase
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.extraSmall),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        repeat(KEYBOARD_ROWS_NUMBER) { lineNumber ->
            Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacers.extraLarge)) {
                keyboardElements.subList(lineNumber * KEYBOARD_ROW_LENGTH, (lineNumber + 1) * KEYBOARD_ROW_LENGTH)
                    .forEach { item: KeyboardItem ->
                        when (item) {
                            is KeyboardItem.Digit -> {
                                KeyboardItemContent(onClick = { onDigit(item.value.toString()) }) {
                                    Text(
                                        text = "${item.value}",
                                        style = MaterialTheme.typography.displaySmall,
                                    )
                                }
                            }

                            KeyboardItem.Erase -> {
                                KeyboardItemContent(onClick = onErase) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_erase_44),
                                        contentDescription = null
                                    )
                                }
                            }

                            KeyboardItem.LogOut -> {
                                KeyboardItemContent(onClick = onLogout) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_logout_44),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }
}

@Composable
private fun KeyboardItemContent(
    onClick: () -> Unit,
    content: @Composable () -> Unit,
) {
    NomiaFilledIconButton(
        onClick = onClick,
        modifier = Modifier
            .clipToBounds()
            .requiredSize(72.dp),
        colors = NomiaButtonDefaults.pinKeyboardColors(),
        content = content
    )
}

private const val KEYBOARD_ROW_LENGTH = 3
private const val KEYBOARD_ROWS_NUMBER = 4

@ThemePreviews
@Composable
private fun KeyboardPreview() {
    NomiaThemeMaterial3 {
        Surface {
            BaseAuthLayout {
                InternalAuthLayout(
                    onDigit = {},
                    onErase = {},
                    onLogout = {},
                    pinProvider = { Code.Pin(isCorrect = false) }
                )
            }
        }
    }
}
