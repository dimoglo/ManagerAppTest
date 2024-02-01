package net.nomia.common.ui.theme.model

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.ContentAlpha
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import net.nomia.common.data.model.Theme

@Composable
fun useDarkTheme(customTheme: Theme?) = customTheme?.let { it == Theme.DARK } ?: isSystemInDarkTheme()

@Composable
fun Color.disableable(isEnabled: Boolean) =
    if (isEnabled) this else this.copy(alpha = ContentAlpha.disabled)

@ColorInt
fun String.toHslColor(
    @FloatRange(from = 0.0, to = 1.0) saturation: Float = 0.5f,
    @FloatRange(from = 0.0, to = 1.0) lightness: Float = 0.5f
): Int {
    val code = this.fold(0) { code, char ->  char.code + ((code shl 5) - code) }
    val hue = (code % 360).toFloat()
    return ColorUtils.HSLToColor(floatArrayOf(hue, saturation, lightness))
}
