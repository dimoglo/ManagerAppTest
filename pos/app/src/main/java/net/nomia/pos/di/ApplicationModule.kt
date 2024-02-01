package net.nomia.pos.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import net.nomia.main.config.MainDatabase
import net.nomia.pos.data.AnalyticsRepositoryImpl
import net.nomia.pos.domain.LogoutUseCase
import net.nomia.pos.domain.LogoutUseCaseImpl
import net.nomia.pos.domain.analytics.AnalyticsRepository
import net.nomia.settings.config.SettingsDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class ApplicationModule {
    @Singleton
    @Provides
    fun applicationScopeProvide(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main)

    @Provides
    fun provideLogoutUseCase(
        settingsDatabase: SettingsDatabase,
        mainDatabase: MainDatabase,
    ): LogoutUseCase = LogoutUseCaseImpl(
        settingsDatabase = settingsDatabase,
        mainDatabase = mainDatabase,
    )
}

@Module
@InstallIn(ViewModelComponent::class)
internal abstract class RepositoryMappingModule {
    @Binds
    abstract fun bindAnalyticsRepository(impl: AnalyticsRepositoryImpl): AnalyticsRepository

}