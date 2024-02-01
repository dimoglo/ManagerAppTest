package net.nomia.core.common.config_provider.domain.usecase

import net.nomia.core.common.state_handler.di.GlobalStateHandler
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import net.nomia.core.common.config_provider.model.Config
import javax.inject.Inject

interface SaveThemeConfigUseCase {
    operator fun invoke(themeType: Config.ThemeType)
}

internal class SaveThemeConfigUseCaseImpl @Inject constructor(
    @GlobalStateHandler private val globalStateHandlerRepository: StateHandlerRepository,
) : SaveThemeConfigUseCase {

    override fun invoke(themeType: Config.ThemeType) {
        globalStateHandlerRepository.save(
            key = Config.ThemeType::class.java.name,
            value = themeType.name,
        )
    }
}
