package net.nomia.pos.domain.auth.pos_setup.use_cases

import kotlinx.coroutines.flow.Flow
import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.ServerProvider
import javax.inject.Inject

interface ServerProviderUseCase {

    fun get(): Flow<ServerProvider>

}

class ServerProviderUseCaseImpl @Inject constructor(
    private val settingsRepository: SettingsRepository,
) : ServerProviderUseCase {

    override fun get(): Flow<ServerProvider> = settingsRepository.getServerProvider()

}
