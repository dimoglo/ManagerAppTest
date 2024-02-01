package net.nomia.common.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import net.nomia.common.ui.theme.paddings

@Composable
fun ColumnScope.NomiaFooter(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Divider()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.paddings.medium)
            .background(color = MaterialTheme.colorScheme.surface)
    ) {
        content()
    }
}
