package net.nomia.core.common.config_provider.presentation

import androidx.compose.runtime.compositionLocalOf
import net.nomia.core.common.config_provider.model.Config

val LocalConfig = compositionLocalOf<Config?> { null }
