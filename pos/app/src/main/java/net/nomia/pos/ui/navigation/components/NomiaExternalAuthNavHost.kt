package net.nomia.pos.ui.navigation.components

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.nomia.pos.ui.navigation.ExternalAuthDestination
import net.nomia.pos.ui.navigation.PosSetupDestination
import net.nomia.pos.ui.auth.external.ExternalAuthScreen
import net.nomia.pos.ui.auth.pos_setup.PosSetupMviScreen
import net.nomia.pos.ui.navigation.argumentList
import net.nomia.pos.ui.navigation.route

@Composable
fun NomiaExternalAuthNavHost(
    navHostController: NavHostController = rememberNavController(),
    widthSizeClass: WindowWidthSizeClass,
) {
    NavHost(
        navController = navHostController,
        startDestination = ExternalAuthDestination.route
    ) {
        composable(
            ExternalAuthDestination.route,
            ExternalAuthDestination.argumentList
        ) {
            ExternalAuthScreen(navController = navHostController)
        }

        composable(
            PosSetupDestination.route,
            PosSetupDestination.argumentList
        ) {
            PosSetupMviScreen(
                navController = navHostController,
                widthSizeClass = widthSizeClass,
            )
        }
    }
}
