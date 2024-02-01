package net.nomia.pos.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import net.nomia.pos.core.provider.ResourcesProvider
import net.nomia.pos.core.provider.ResourcesProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal interface CoreModule {

    @Binds
    @Singleton
    fun bind(resourcesProviderImpl: ResourcesProviderImpl): ResourcesProvider
}
