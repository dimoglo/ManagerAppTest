package net.nomia.pos.ui.navigation.components

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.nomia.pos.ui.navigation.InternalAuthDestination
import net.nomia.pos.ui.auth.internal.InternalAuthScreen
import net.nomia.pos.ui.navigation.route

@Composable
internal fun NomiaInternalAuthNavHost(
    navHostController: NavHostController = rememberNavController(),
) {
    NavHost(navController = navHostController, startDestination = InternalAuthDestination.route) {
        composable(route = InternalAuthDestination.route) {
            InternalAuthScreen()
        }
    }
}
