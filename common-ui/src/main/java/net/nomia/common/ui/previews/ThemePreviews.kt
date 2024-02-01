package net.nomia.common.ui.previews

import android.content.res.Configuration
import androidx.compose.ui.tooling.preview.Preview

@Preview(group = "compact", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, name = "Light theme")
@Preview(group = "compact", showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Dark theme")
annotation class ThemePreviews
