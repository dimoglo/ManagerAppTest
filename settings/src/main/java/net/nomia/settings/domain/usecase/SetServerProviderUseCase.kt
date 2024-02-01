package net.nomia.settings.domain.usecase

import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.ServerProvider
import javax.inject.Inject

interface SetServerProviderUseCase {
    suspend operator fun invoke(serverProvider: ServerProvider)
}

internal class SetServerProviderUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : SetServerProviderUseCase {

    override suspend fun invoke(serverProvider: ServerProvider) {
        settingsRepository.saveServerProvider(serverProvider = serverProvider)
    }
}
