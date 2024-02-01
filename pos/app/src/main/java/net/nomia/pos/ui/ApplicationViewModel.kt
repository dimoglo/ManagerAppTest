package net.nomia.pos.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import net.nomia.common.data.Constants.SharingStartedWithDefaultTimeout
import net.nomia.main.domain.PrincipalRepository
import net.nomia.pos.ui.navigation.model.AppStartDestination
import net.nomia.settings.domain.SettingsRepository
import javax.inject.Inject

@Suppress("LongParameterList")
@HiltViewModel
class ApplicationViewModel @Inject constructor(
    principalRepository: PrincipalRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    val customTheme = settingsRepository.getAppearance()
        .map { if (it.useCustomTheme) it.theme else null }
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val appStartDestination = combine(
        principalRepository.currentPrincipal.map { it != null },
        settingsRepository.getApplicationToken().map { it != null },
    ) { isUserAuthenticated, hasToken ->
        when {
            !hasToken -> AppStartDestination.ExternalAuth
            !isUserAuthenticated -> AppStartDestination.InternalAuth
            else -> AppStartDestination.Authorized
        }
    }.stateIn(viewModelScope, SharingStartedWithDefaultTimeout, AppStartDestination.ExternalAuth)
}
