package net.nomia.settings.domain.usecase

import kotlinx.coroutines.flow.Flow
import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.ServerProvider
import javax.inject.Inject

interface ObserveServerProviderUseCase {
    operator fun invoke(): Flow<ServerProvider>
}

internal class ObserveServerProviderUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ObserveServerProviderUseCase {

    override fun invoke(): Flow<ServerProvider> = settingsRepository.getServerProvider()
}
