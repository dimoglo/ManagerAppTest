package net.nomia.common.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import net.nomia.common.ui.R
import net.nomia.common.ui.theme.model.AppResources
import net.nomia.common.ui.theme.model.Blue
import net.nomia.common.ui.theme.model.DarkOnSuccess
import net.nomia.common.ui.theme.model.DarkOnSuccessContainer
import net.nomia.common.ui.theme.model.DarkOnWarning
import net.nomia.common.ui.theme.model.DarkOnWarningContainer
import net.nomia.common.ui.theme.model.DarkSuccess
import net.nomia.common.ui.theme.model.DarkSuccessContainer
import net.nomia.common.ui.theme.model.DarkWarning
import net.nomia.common.ui.theme.model.DarkWarningContainer
import net.nomia.common.ui.theme.model.Green
import net.nomia.common.ui.theme.model.Lavender
import net.nomia.common.ui.theme.model.LightBlue
import net.nomia.common.ui.theme.model.LightOnSuccess
import net.nomia.common.ui.theme.model.LightOnSuccessContainer
import net.nomia.common.ui.theme.model.LightOnWarning
import net.nomia.common.ui.theme.model.LightOnWarningContainer
import net.nomia.common.ui.theme.model.LightSuccess
import net.nomia.common.ui.theme.model.LightSuccessContainer
import net.nomia.common.ui.theme.model.LightWarning
import net.nomia.common.ui.theme.model.LightWarningContainer
import net.nomia.common.ui.theme.model.MenuSectionColor
import net.nomia.common.ui.theme.model.Mint
import net.nomia.common.ui.theme.model.NomiaColor
import net.nomia.common.ui.theme.model.Orange
import net.nomia.common.ui.theme.model.Paddings
import net.nomia.common.ui.theme.model.Pink
import net.nomia.common.ui.theme.model.Purple
import net.nomia.common.ui.theme.model.Red
import net.nomia.common.ui.theme.model.Spacers
import net.nomia.common.ui.theme.model.Yellow


private val LightColors = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    inversePrimary = LightInversePrimary,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    surfaceTint = LightSurfaceTint,
    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,
    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,
    outline = LightOutline,
    outlineVariant = LightOutlineVariant,
    scrim = LightScrim
)

private val DarkColors = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    inversePrimary = DarkInversePrimary,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    surfaceTint = DarkSurfaceTint,
    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,
    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    scrim = DarkScrim
)

private val DarkAppResource = AppResources(
    logoResId = R.drawable.ic_logo_72,
    textLogoResId = R.drawable.ic_logo_text_dark,
    menuSectionPlaceholderResId = R.drawable.ic_menu_section_dark,
    menuItemPlaceholderResId = R.drawable.ic_menu_item_dark,
)

private val LightAppResource = AppResources(
    logoResId = R.drawable.ic_logo_72,
    textLogoResId = R.drawable.ic_logo_text_light,
    menuSectionPlaceholderResId = R.drawable.ic_menu_section_light,
    menuItemPlaceholderResId = R.drawable.ic_menu_item_light,
)

private val LightExtraColor = NomiaColor(
    warning = LightWarning,
    onWarning = LightOnWarning,
    warningContainer = LightWarningContainer,
    onWarningContainer = LightOnWarningContainer,
    success = LightSuccess,
    onSuccess = LightOnSuccess,
    successContainer = LightSuccessContainer,
    onSuccessContainer = LightOnSuccessContainer
)
private val DarkExtraColor = NomiaColor(
    warning = DarkWarning,
    onWarning = DarkOnWarning,
    warningContainer = DarkWarningContainer,
    onWarningContainer = DarkOnWarningContainer,
    success = DarkSuccess,
    onSuccess = DarkOnSuccess,
    successContainer = DarkSuccessContainer,
    onSuccessContainer = DarkOnSuccessContainer
)

private val MenuSectionColors = MenuSectionColor(
    lightBlue = LightBlue,
    lavender = Lavender,
    blue = Blue,
    orange = Orange,
    pink = Pink,
    red = Red,
    purple = Purple,
    mint = Mint,
    green = Green,
    yellow = Yellow

)

private val LocalAppResources = staticCompositionLocalOf<AppResources> {
    error("CompositionLocal LocalAppResources not present")
}

private val LocalExtraColor = staticCompositionLocalOf<NomiaColor> {
    error("CompositionLocal LocalExtraColor not present")
}

private val LocalMenuSectionColor = staticCompositionLocalOf<MenuSectionColor> {
    error("CompositionLocal LocalMenuSectionColor not present")
}

private val LocalPaddings = staticCompositionLocalOf<Paddings> {
    error("CompositionLocal LocalDimensions not present")
}

private val LocalSpacers =staticCompositionLocalOf<Spacers> {
    error("CompositionLocal LocalSpacers not present")
}


val MaterialTheme.appResources: AppResources
    @Composable
    @ReadOnlyComposable
    get() = LocalAppResources.current

val MaterialTheme.extraColor: NomiaColor
    @Composable
    @ReadOnlyComposable
    get() = LocalExtraColor.current

val MaterialTheme.menuSectionColor: MenuSectionColor
    @Composable
    @ReadOnlyComposable
    get() = LocalMenuSectionColor.current

val MaterialTheme.paddings: Paddings
    @Composable
    @ReadOnlyComposable
    get() = LocalPaddings.current

val MaterialTheme.spacers: Spacers
    @Composable
    @ReadOnlyComposable
    get() = LocalSpacers.current

// TODO: Rename to NomiaTheme after redesign
@Composable
fun NomiaThemeMaterial3(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val (colors, appResources, extraColor) = if (useDarkTheme) {
        Triple(
            first = DarkColors,
            second = DarkAppResource,
            third = DarkExtraColor
        )
    } else {
        Triple(
            first = LightColors,
            second = LightAppResource,
            third = LightExtraColor
        )
    }

    MaterialTheme(
        colorScheme = colors,
        typography = NomiaTypography,
    ) {
        CompositionLocalProvider(
            LocalAppResources provides appResources,
            LocalExtraColor provides extraColor,
            LocalMenuSectionColor provides MenuSectionColors,
            LocalPaddings provides Paddings,
            LocalSpacers provides Spacers
        ) {
            content()
        }
    }
}
