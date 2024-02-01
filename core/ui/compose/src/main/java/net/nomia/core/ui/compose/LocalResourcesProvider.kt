package net.nomia.core.ui.compose

import androidx.compose.runtime.staticCompositionLocalOf
import net.nomia.pos.core.provider.ResourcesProvider

val LocalResourcesProvider = staticCompositionLocalOf<ResourcesProvider?> { null }
