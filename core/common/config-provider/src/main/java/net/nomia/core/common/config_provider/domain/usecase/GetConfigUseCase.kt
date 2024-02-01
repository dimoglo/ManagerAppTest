package net.nomia.core.common.config_provider.domain.usecase

import net.nomia.core.common.state_handler.di.GlobalStateHandler
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import net.nomia.core.common.config_provider.model.Config
import javax.inject.Inject

interface GetConfigUseCase {
    operator fun invoke(): Config
}

internal class GetConfigUseCaseImpl @Inject constructor(
    @GlobalStateHandler private val globalStateHandlerRepository: StateHandlerRepository,
) : GetConfigUseCase {

    override fun invoke(): Config {
        val nameThemeType = checkNotNull(
            globalStateHandlerRepository.get(
                key = Config.ThemeType::class.java.name,
                clazz = String::class,
            )
        )

        val nameDisplayType = checkNotNull(
            globalStateHandlerRepository.get(
                key = Config.DisplayType::class.java.name,
                clazz = String::class,
            )
        )

        val themeType = Config.ThemeType.valueOf(nameThemeType)

        val displayType = Config.DisplayType.valueOf(nameDisplayType)

        return Config(displayType = displayType, themeType = themeType)
    }
}
