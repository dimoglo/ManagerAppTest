package net.nomia.common.ui.composable

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.previews.ThemePreviews
import net.nomia.common.ui.theme.NomiaThemeMaterial3

@Composable
fun NomiaFilledButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = NomiaButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun NomiaTonalButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    contentPadding: PaddingValues = NomiaButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
}

@Composable
fun NomiaTonalButtonWithIcon(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.filledTonalButtonColors(),
    contentPadding: PaddingValues = NomiaButtonDefaults.ButtonWithIconContentPadding,
    leadingIcon: @Composable () -> Unit,
    content: @Composable RowScope.() -> Unit,
) {
   FilledTonalButton(
       onClick = onClick,
       modifier = modifier,
       enabled = enabled,
       colors = colors,
       contentPadding = contentPadding,
       content = {
           leadingIcon()
           Spacer(Modifier.width(ButtonDefaults.IconSpacing))
           content()
       }
   )
}

@Composable
fun NomiaFilledIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = IconButtonDefaults.filledShape,
    colors: IconButtonColors = IconButtonDefaults.filledTonalIconButtonColors(),
    content: @Composable () -> Unit
) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        content = content
    )
}

object NomiaButtonDefaults {
    private val buttonHorizontalPadding = 24.dp
    private val buttonVerticalPadding = 10.dp
    private val buttonWithIconHorizontalStartPadding = 16.dp

    val ContentPadding =
        PaddingValues(
            start = buttonHorizontalPadding,
            top = buttonVerticalPadding,
            end = buttonHorizontalPadding,
            bottom = buttonVerticalPadding
        )

    val ButtonWithIconContentPadding =
        PaddingValues(
            start = buttonWithIconHorizontalStartPadding,
            top = buttonVerticalPadding,
            end = buttonHorizontalPadding,
            bottom = buttonVerticalPadding
        )

    @Composable
    fun pinKeyboardColors() = IconButtonDefaults.filledIconButtonColors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@ThemePreviews
@Composable
private fun NomiaFilledButtonPreview() {
    NomiaThemeMaterial3 {
        Surface {
            Row {
                NomiaFilledButton(onClick = {}) {
                    Text(text = "Enabled")
                }
                Spacer(modifier = Modifier.width(8.dp))
                NomiaFilledButton(onClick = {}, enabled = false) {
                    Text(text = "Disabled")
                }
            }
        }
    }
}

@ThemePreviews
@Composable
private fun NomiaTonalButtonPreview() {
    NomiaThemeMaterial3 {
        Surface {
            Row {
                NomiaTonalButton(onClick = {}) {
                    Text(text = "Enabled")
                }
                Spacer(modifier = Modifier.width(8.dp))
                NomiaTonalButton(onClick = {}, enabled = false) {
                    Text(text = "Disabled")
                }
            }
        }
    }
}
