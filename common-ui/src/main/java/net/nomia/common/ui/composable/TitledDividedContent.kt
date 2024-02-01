package net.nomia.common.ui.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Divider
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.intl.LocaleList
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.theme.spacers
import java.util.Locale

@Composable
fun TitledDividedContent(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    BasicTitledDividedContent(
        modifier = modifier,
        titleContent = { Text(text = title.uppercase(Locale.getDefault())) },
        content = content,
    )
}

@Composable
fun TitledDividedContent(
    modifier: Modifier = Modifier,
    title: AnnotatedString,
    content: @Composable ColumnScope.() -> Unit,
) {
    BasicTitledDividedContent(
        modifier = modifier,
        titleContent = { Text(text = title.toUpperCase(LocaleList.current)) },
        content = content,
    )
}

@Composable
private fun BasicTitledDividedContent(
    modifier: Modifier = Modifier,
    titleContent: @Composable ColumnScope.() -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(modifier = modifier) {
        CompositionLocalProvider(
            LocalTextStyle provides MaterialTheme.typography.labelSmall,
            LocalContentColor provides MaterialTheme.colorScheme.onSurfaceVariant,
        ) {
            titleContent()
        }

        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))

        Divider()
        content()
    }
}

object TitledDividedDefaults {
    val spacer: Dp = 20.dp
}
