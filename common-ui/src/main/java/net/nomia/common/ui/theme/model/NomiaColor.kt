package net.nomia.common.ui.theme.model

import androidx.compose.ui.graphics.Color

/* Custom light colors */
internal val LightWarning = Color(0xFFC5831B)
internal val LightOnWarning = Color(0xFFFFFFFF)
internal val LightWarningContainer = Color(0xFFFFDDB7)
internal val LightOnWarningContainer = Color(0xFF2A1700)
internal val LightSuccess = Color(0xFF006D44)
internal val LightOnSuccess = Color(0xFFFFFFFF)
internal val LightSuccessContainer = Color(0xFF91F7BE)
internal val LightOnSuccessContainer = Color(0xFF002111)

/* Custom dark colors */
internal val DarkWarning = Color(0xFFFFB95C)
internal val DarkOnWarning = Color(0xFF462A00)
internal val DarkWarningContainer = Color(0xFF653E00)
internal val DarkOnWarningContainer = Color(0xFFFFDDB7)
internal val DarkSuccess = Color(0xFF75DAA3)
internal val DarkOnSuccess = Color(0xFF003921)
internal val DarkSuccessContainer = Color(0xFF005232)
internal val DarkOnSuccessContainer = Color(0xFF91F7BE)

class NomiaColor(
    val warning: Color,
    val onWarning: Color,
    val warningContainer: Color,
    val onWarningContainer: Color,
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)
