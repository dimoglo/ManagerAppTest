package net.nomia.core.common.state_handler.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import net.nomia.core.common.state_handler.data.repository.GlobalStateHandlerRepositoryImpl
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GlobalStateHandler

@Module
@InstallIn(SingletonComponent::class)
internal interface GlobalStateHandlerModule {

    @Binds
    @Singleton
    @GlobalStateHandler
    fun bindStateHandlerRepository(
        globalStateHandlerRepository: GlobalStateHandlerRepositoryImpl,
    ): StateHandlerRepository

    companion object {
        @Qualifier
        @Retention(AnnotationRetention.BINARY)
        annotation class SharedPreferenceGlobalStateHandler

        @Provides
        @Singleton
        @SharedPreferenceGlobalStateHandler
        fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences =
            context.getSharedPreferences("SharedPreferenceGlobalStateHandler", Context.MODE_PRIVATE)
    }
}
