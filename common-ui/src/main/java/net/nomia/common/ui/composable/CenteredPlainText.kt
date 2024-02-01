package net.nomia.common.ui.composable

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import net.nomia.common.ui.theme.paddings

@Composable
fun CenteredPlainText(
    modifier: Modifier,
    text: String
) {
    CompositionLocalProvider(LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant) {
        ProvideTextStyle(MaterialTheme.typography.bodyLarge) {
            Text(
                modifier = modifier.padding(MaterialTheme.paddings.medium),
                text = text,
                textAlign = TextAlign.Center
            )
        }
    }
}
