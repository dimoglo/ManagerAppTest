package net.nomia.common.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.theme.paddings

@Composable
fun TextChip(
    modifier: Modifier = Modifier,
    text: String,
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
) {
    NomiaChip(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        Text(text = text)
    }
}

@Composable
fun IconTextChip(
    modifier: Modifier = Modifier,
    icon: Painter?,
    text: String,
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
) {
    NomiaChip(
        modifier = modifier,
        containerColor = containerColor,
        contentColor = contentColor,
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = icon,
                contentDescription = null,
            )
        }

        Text(text = text)
    }
}

@Composable
fun NomiaChip(
    modifier: Modifier = Modifier,
    containerColor: Color,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable RowScope.() -> Unit,
) {
    Row(
        modifier = modifier
            .background(
                color = containerColor,
                shape = MaterialTheme.shapes.medium,
            )
            .padding(
                horizontal = MaterialTheme.paddings.small,
                vertical = MaterialTheme.paddings.extraSmall / 2,
            )
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.extraSmall),
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides MaterialTheme.typography.labelLarge,
        ) {
            content()
        }
    }
}
