package net.nomia.core.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressIndicatorWithBackground(
    progress: Float = 1f,
    color: Color = ProgressIndicatorDefaults.circularColor,
    backgroundColor: Color = Color.Transparent,
    size: Dp = 24.dp,
    strokeWidth: Dp = 2.dp,
) {
    Box {
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            strokeWidth = strokeWidth,
            progress = 1f,
            color = backgroundColor,
        )
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            strokeWidth = strokeWidth,
            progress = progress,
            color = color,
        )
    }
}

@Preview
@Composable
private fun Preview() = CircularProgressIndicatorWithBackground(
    progress = 0.3f,
    color = Color.Red,
    backgroundColor = Color.Blue,
)
