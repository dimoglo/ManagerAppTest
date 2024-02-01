package net.nomia.settings.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.nomia.settings.data.SettingsRepositoryImpl
import net.nomia.settings.domain.SettingsRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class SettingsDataModule {

    @Singleton
    @Binds
    abstract fun bindSettingsRepository(
        settingsRepositoryImpl: SettingsRepositoryImpl
    ): SettingsRepository

}
