package net.nomia.core.common.config_provider.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.nomia.core.common.config_provider.domain.usecase.GetConfigUseCase
import net.nomia.core.common.config_provider.domain.usecase.GetConfigUseCaseImpl
import net.nomia.core.common.config_provider.domain.usecase.ObserveConfigUseCase
import net.nomia.core.common.config_provider.domain.usecase.ObserveConfigUseCaseImpl
import net.nomia.core.common.config_provider.domain.usecase.SaveConfigUseCase
import net.nomia.core.common.config_provider.domain.usecase.SaveConfigUseCaseImpl
import net.nomia.core.common.config_provider.domain.usecase.SaveThemeConfigUseCase
import net.nomia.core.common.config_provider.domain.usecase.SaveThemeConfigUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface ConfigModule {

    @Binds
    fun bindGetConfigUseCase(getConfigUseCase: GetConfigUseCaseImpl): GetConfigUseCase

    @Binds
    fun bindObserveConfigUseCase(observeConfigUseCase: ObserveConfigUseCaseImpl): ObserveConfigUseCase

    @Binds
    fun bindSaveConfigUseCase(saveConfigUseCase: SaveConfigUseCaseImpl): SaveConfigUseCase

    @Binds
    fun bindSaveThemeConfigUseCase(saveThemeConfigUseCase: SaveThemeConfigUseCaseImpl): SaveThemeConfigUseCase
}
