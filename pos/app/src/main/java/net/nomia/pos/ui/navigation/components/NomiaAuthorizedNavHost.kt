package net.nomia.pos.ui.navigation.components


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.nomia.pos.ui.navigation.ManagerScreenDestination
import net.nomia.pos.ui.analytics.AnalyticsScreen
import net.nomia.pos.ui.navigation.argumentList
import net.nomia.pos.ui.navigation.route

@Composable
internal fun NomiaAuthorizedNavHost(
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navHostController,
        startDestination = ManagerScreenDestination.route
    ) {
        composable(
            route = ManagerScreenDestination.route,
            arguments = ManagerScreenDestination.argumentList
        ) {
            AnalyticsScreen()
        }
    }
}


