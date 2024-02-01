@file:Suppress("unused")

package net.nomia.pos.ui.navigation

import com.compose.type_safe_args.annotation.ComposeDestination

@ComposeDestination
interface PosSetupDestination {
    companion object
}

@ComposeDestination
interface ExternalAuthDestination {
    companion object
}

@ComposeDestination
interface InternalAuthDestination {
    companion object
}

@ComposeDestination
interface ManagerScreenDestination {
    companion object
}
