package net.nomia.core.common.config_provider.domain.usecase

import net.nomia.core.common.state_handler.di.GlobalStateHandler
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import net.nomia.core.common.config_provider.model.Config
import javax.inject.Inject

interface SaveConfigUseCase {
    operator fun invoke(config: Config)
}

internal class SaveConfigUseCaseImpl @Inject constructor(
    @GlobalStateHandler private val globalStateHandlerRepository: StateHandlerRepository,
) : SaveConfigUseCase {

    override fun invoke(config: Config) {
        globalStateHandlerRepository.save(
            key = Config.DisplayType::class.java.name,
            value = config.displayType.name,
        )
        globalStateHandlerRepository.save(
            key = Config.ThemeType::class.java.name,
            value = config.themeType.name,
        )
    }
}
