package net.nomia.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.MenuAppearance
import javax.inject.Inject


interface ObserveMenuAppearanceUseCase {
    operator fun invoke(): Flow<MenuAppearance>
}

internal class ObserveMenuAppearanceUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ObserveMenuAppearanceUseCase {

    override fun invoke(): Flow<MenuAppearance> = settingsRepository.getMenuAppearance()
}
