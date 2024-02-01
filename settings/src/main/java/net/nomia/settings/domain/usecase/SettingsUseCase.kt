package net.nomia.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.AppearanceData
import javax.inject.Inject

interface SettingsUseCase {
    fun observe(): Flow<AppearanceData>
}

internal class SettingsUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : SettingsUseCase {

    override fun observe(): Flow<AppearanceData> = settingsRepository.getAppearance()

}
