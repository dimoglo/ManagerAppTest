package net.nomia.pos.ui.auth.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import net.nomia.common.ui.theme.paddings


@Composable
internal fun CodeField(
    size: Int,
    onValueChange: DrawScope.(codePosition: Int) -> Unit,
) {
    Row(horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.medium)) {
        repeat(size) { position ->
            CodeDot {
                onValueChange(position + 1)
            }
        }
    }
}

@Composable
private fun CodeDot(onValueChange: DrawScope.() -> Unit) {
    Box(
        modifier = Modifier
            .size(16.dp)
            .background(color = Color.Unspecified)
            .drawBehind(onDraw = onValueChange)
    )
}
