package net.nomia.core.ui.status_bar_controller

import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.SystemUiController

fun SystemUiController.setThemeStatusBar(
    isDark: Boolean,
    color: Color? = null,
) {
    setSystemBarsColor(
        color = color ?: Color.Transparent,
        darkIcons = !isDark,
        isNavigationBarContrastEnforced = false,
    )
}

fun SystemUiController.hideStatusBar() {
    setSystemBarsColor(
        color = Color.Transparent,
        isNavigationBarContrastEnforced = false,
    )
}
