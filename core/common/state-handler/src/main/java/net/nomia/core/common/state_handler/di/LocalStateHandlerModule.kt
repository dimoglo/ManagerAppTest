package net.nomia.core.common.state_handler.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import net.nomia.core.common.state_handler.data.repository.LocalStateHandlerRepositoryImpl
import net.nomia.core.common.state_handler.domain.repository.StateHandlerRepository
import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocalStateHandler

@Module
@InstallIn(ViewModelComponent::class)
internal interface LocalStateHandlerModule {

    @Binds
    @ViewModelScoped
    @LocalStateHandler
    fun bindStateHandlerRepository(
        localStateHandlerRepository: LocalStateHandlerRepositoryImpl,
    ): StateHandlerRepository
}

