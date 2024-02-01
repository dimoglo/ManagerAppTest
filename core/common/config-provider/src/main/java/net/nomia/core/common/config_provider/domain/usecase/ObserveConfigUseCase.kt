package net.nomia.core.common.config_provider.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import net.nomia.core.common.state_handler.di.GlobalStateHandler
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import net.nomia.core.common.config_provider.model.Config
import javax.inject.Inject

interface ObserveConfigUseCase {
    operator fun invoke(): Flow<Config>
}

internal class ObserveConfigUseCaseImpl @Inject constructor(
    @GlobalStateHandler private val globalStateHandlerRepository: StateHandlerRepository,
) : ObserveConfigUseCase {

    override fun invoke(): Flow<Config> = combine(
        globalStateHandlerRepository
            .observe(
                key = Config.ThemeType::class.java.name,
                clazz = String::class,
            )
            .filterNotNull()
            .map { Config.ThemeType.valueOf(it) },

        globalStateHandlerRepository
            .observe(
                key = Config.DisplayType::class.java.name,
                clazz = String::class,
            )
            .filterNotNull()
            .map { Config.DisplayType.valueOf(it) }

    ) { themeType, displayType ->
        Config(
            displayType = displayType,
            themeType = themeType,
        )
    }
}
