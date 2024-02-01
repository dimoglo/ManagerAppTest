@file:Suppress("TooManyFunctions")

package net.nomia.pos.ui

import android.app.UiModeManager
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import net.nomia.common.ui.theme.NomiaThemeMaterial3
import net.nomia.common.ui.theme.model.useDarkTheme
import net.nomia.core.ui.compose.LocalResourcesProvider
import net.nomia.pos.R
import net.nomia.pos.core.provider.ResourcesProvider
import net.nomia.pos.ui.navigation.components.NomiaAuthorizedNavHost
import net.nomia.pos.ui.navigation.components.NomiaExternalAuthNavHost
import net.nomia.pos.ui.navigation.components.NomiaInternalAuthNavHost
import net.nomia.pos.ui.navigation.model.AppStartDestination
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var resourcesProvider: ResourcesProvider

    @Suppress("LongMethod")
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val isTablet = resources.getBoolean(R.bool.isTablet)
        super.onCreate(savedInstanceState)

        requestedOrientation = if (isTablet) {
            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

            val applicationViewModel: ApplicationViewModel = hiltViewModel()
            val customTheme by applicationViewModel.customTheme.collectAsState()
            val useDarkTheme = useDarkTheme(customTheme)
            val appStartDestination by applicationViewModel.appStartDestination.collectAsState()

            CompositionLocalProvider(
                LocalResourcesProvider provides resourcesProvider,
            ) {
                NomiaThemeMaterial3(useDarkTheme = useDarkTheme) {
                    Surface(
                        Modifier
                            .navigationBarsPadding()
                            .imePadding()
                    ) {
                        val systemUiController = rememberSystemUiController()
                        val surfaceColor = MaterialTheme.colorScheme.surface
                        val navBarColor =
                            MaterialTheme.colorScheme.surfaceColorAtElevation(NavigationBarDefaults.Elevation)

                        LaunchedEffect(useDarkTheme) {
                            systemUiController.run {
                                setStatusBarColor(color = surfaceColor)
                                setNavigationBarColor(color = navBarColor)
                            }
                        }

                        when (appStartDestination) {
                            AppStartDestination.ExternalAuth ->
                                NomiaExternalAuthNavHost(widthSizeClass = widthSizeClass)

                            AppStartDestination.InternalAuth ->
                                NomiaInternalAuthNavHost()

                            AppStartDestination.Authorized ->
                                NomiaAuthorizedNavHost()
                        }
                    }
                }
            }
        }
    }
}
