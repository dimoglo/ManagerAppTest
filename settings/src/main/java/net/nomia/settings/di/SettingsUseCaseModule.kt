package net.nomia.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.nomia.settings.domain.usecase.ObserveCurrencyUseCase
import net.nomia.settings.domain.usecase.ObserveCurrencyUseCaseImpl
import net.nomia.settings.domain.usecase.ObserveMenuAppearanceUseCase
import net.nomia.settings.domain.usecase.ObserveMenuAppearanceUseCaseImpl
import net.nomia.settings.domain.usecase.ObserveServerProviderUseCase
import net.nomia.settings.domain.usecase.ObserveServerProviderUseCaseImpl
import net.nomia.settings.domain.usecase.SetServerProviderUseCase
import net.nomia.settings.domain.usecase.SetServerProviderUseCaseImpl
import net.nomia.settings.domain.usecase.SettingsUseCase
import net.nomia.settings.domain.usecase.SettingsUseCaseImpl

@Module
@InstallIn(SingletonComponent::class)
internal interface SettingsUseCaseModule {

    @Binds
    fun bindSetServerProviderUseCase(
        setServerProviderUseCase: SetServerProviderUseCaseImpl
    ): SetServerProviderUseCase

    @Binds
    fun bindObserveServerProviderUseCase(
        getServerProviderUseCase: ObserveServerProviderUseCaseImpl
    ): ObserveServerProviderUseCase

    @Binds
    fun bindObserveMenuAppearanceUseCase(
        observeMenuAppearanceUseCase: ObserveMenuAppearanceUseCaseImpl
    ): ObserveMenuAppearanceUseCase

    @Binds
    fun bindObserveCurrencyUseCase(
        observeCurrencyUseCase: ObserveCurrencyUseCaseImpl
    ): ObserveCurrencyUseCase

    @Binds
    fun bindSettingsUseCase(
        settingsUseCase: SettingsUseCaseImpl,
    ): SettingsUseCase

}
