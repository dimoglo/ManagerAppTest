package net.nomia.pos.ui.auth.external.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.theme.appResources
import net.nomia.common.ui.theme.spacers

@Composable
internal fun ExternalAuthContent(
    isCenterAligned: Boolean = false,
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    Column(horizontalAlignment = if (isCenterAligned) Alignment.CenterHorizontally else Alignment.Start) {
        Icon(
            painter = painterResource(id = MaterialTheme.appResources.textLogoResId),
            tint = Color.Unspecified,
            contentDescription = null
        )

        Spacer(modifier = Modifier.height(40.dp))

        TitleAndSubtitle(
            isCenterAligned = isCenterAligned,
            title = title,
            subtitle = subtitle
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))
    }

    content()
}

@Composable
private fun TitleAndSubtitle(
    isCenterAligned: Boolean,
    title: String,
    subtitle: String,
) {
    val modifier = if (isCenterAligned) Modifier.fillMaxWidth() else Modifier
    val textAlign = if (isCenterAligned) TextAlign.Center else null

    Column(modifier = modifier) {
        Text(
            modifier = modifier,
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = textAlign,
        )

        Spacer(modifier = Modifier.height(MaterialTheme.spacers.small))

        Text(
            modifier = modifier,
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = textAlign,
        )
    }
}
