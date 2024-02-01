package net.nomia.core.ui.preview

import android.content.res.Configuration
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import net.nomia.common.ui.theme.NomiaThemeMaterial3

@Preview(
    group = "LightPhone",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "LightPhone",
    device = Devices.PHONE,
)
@Preview(
    group = "NightPhone",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "NightPhone",
    device = Devices.PHONE,
)
annotation class PhonePreview

@Preview(
    group = "LightTablet",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "LightTablet",
    device = Devices.TABLET,
)
@Preview(
    group = "NightTablet",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "NightTablet",
    device = Devices.TABLET,
)
annotation class TabletPreview

@PhonePreview
@TabletPreview
annotation class PhoneAndTabletPreview

@Preview(
    group = "LightItem",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    name = "LightItem",
    device = Devices.PHONE,
)
@Preview(
    group = "NightItem",
    showBackground = false,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    name = "NightItem",
    device = Devices.PHONE,
)
annotation class ItemPreview

@Composable
fun ItemPreview(content: @Composable () -> Unit) {
    NomiaThemeMaterial3(
        useDarkTheme = isSystemInDarkTheme(),
    ) {
        Box {
            content()
        }
    }
}

@Composable
fun FullScreenPreview(content: @Composable () -> Unit) {
    NomiaThemeMaterial3(
        useDarkTheme = isSystemInDarkTheme(),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Box {
                content()
            }
        }
    }
}
