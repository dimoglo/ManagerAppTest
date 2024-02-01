@file:OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("LongParameterList")

package net.nomia.common.ui.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import net.nomia.common.ui.R
import net.nomia.common.ui.composable.NomiaDialogDefaults.alertDialogColors
import net.nomia.common.ui.theme.paddings

@Composable
fun NomiaProgressAlertDialog(
    modifier: Modifier = Modifier,
    isInProgress: Boolean,
    title: String? = null,
    text: String? = null,
    confirmText: String,
    dismissText: String? = null,
    shape: Shape = AlertDialogDefaults.shape,
    defaults: AlertDialogColors = alertDialogColors(),
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties(),
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        shape = shape,
        containerColor = defaults.containerColor,
        iconContentColor = defaults.iconContentColor,
        titleContentColor = defaults.titleContentColor,
        textContentColor = defaults.textContentColor,
        tonalElevation = tonalElevation,
        properties = properties,
        title = {
            title?.let { Text(it) }
        },
        text = {
            text?.let { Text(it) }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                enabled = !isInProgress,
                content = {
                    ProgressText(
                        text = confirmText,
                        isInProgress = isInProgress,
                        spinnerSize = NomiaSpinnerDefaults.alertDialogSize,
                    )
                },
            )
        },
        dismissButton = {
            dismissText?.let {
                TextButton(
                    onClick = onDismiss,
                    enabled = !isInProgress,
                    content = { Text(it) }
                )
            }
        }
    )

}

@Composable
fun NomiaMessageAlertDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    icon: (@Composable () -> Unit)? = null,
    title: (@Composable () -> Unit)? = null,
    text: @Composable (() -> Unit)?,
    shape: Shape = AlertDialogDefaults.shape,
    containerColor: Color = AlertDialogDefaults.containerColor,
    iconContentColor: Color = AlertDialogDefaults.iconContentColor,
    titleContentColor: Color = AlertDialogDefaults.titleContentColor,
    textContentColor: Color = AlertDialogDefaults.textContentColor,
    tonalElevation: Dp = AlertDialogDefaults.TonalElevation,
    properties: DialogProperties = DialogProperties()
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        Surface(
            modifier = modifier,
            shape = shape,
            color = containerColor,
            tonalElevation = tonalElevation,
        ) {
            Column(
                modifier = Modifier
                    .sizeIn(minWidth = 280.dp, maxWidth = 560.dp)
                    .padding(MaterialTheme.paddings.large)
            ) {
                icon?.let {
                    CompositionLocalProvider(LocalContentColor provides iconContentColor) {
                        Box(
                            Modifier
                                .padding(bottom = MaterialTheme.paddings.medium)
                                .align(Alignment.CenterHorizontally)
                        ) {
                            icon()
                        }
                    }
                }
                title?.let {
                    CompositionLocalProvider(LocalContentColor provides titleContentColor) {
                        ProvideTextStyle(MaterialTheme.typography.headlineSmall) {
                            Box(
                                // Align the title to the center when an icon is present.
                                Modifier
                                    .padding(bottom = MaterialTheme.paddings.medium)
                                    .align(
                                        if (icon == null) {
                                            Alignment.Start
                                        } else {
                                            Alignment.CenterHorizontally
                                        }
                                    )
                            ) {
                                title()
                            }
                        }
                    }
                }
                text?.let {
                    CompositionLocalProvider(LocalContentColor provides textContentColor) {
                        ProvideTextStyle(MaterialTheme.typography.bodyMedium) {
                            Box(
                                Modifier
                                    .weight(weight = 1f, fill = false)
                                    .align(Alignment.Start)
                            ) {
                                text()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LogoutDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        title = { Text(text = stringResource(id = R.string.settings_logout_dialog_title)) },
        text = { Text(text = stringResource(id = R.string.settings_logout_dialog_body)) },
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(text = stringResource(R.string.settings_logout_dialog_dismiss))
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(text = stringResource(R.string.settings_logout_dialog_confirm))
            }
        },
    )
}

object NomiaDialogDefaults {
    @Composable
    fun alertDialogColors(
        containerColor: Color = AlertDialogDefaults.containerColor,
        iconContentColor: Color = AlertDialogDefaults.iconContentColor,
        titleContentColor: Color = AlertDialogDefaults.titleContentColor,
        textContentColor: Color = AlertDialogDefaults.textContentColor,
    ) = AlertDialogColors(
        containerColor = containerColor,
        iconContentColor = iconContentColor,
        titleContentColor = titleContentColor,
        textContentColor = textContentColor,
    )
}

data class AlertDialogColors(
    internal val containerColor: Color,
    internal val iconContentColor: Color,
    internal val titleContentColor: Color,
    internal val textContentColor: Color,
)
