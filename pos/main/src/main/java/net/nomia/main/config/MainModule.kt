package net.nomia.main.config

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoSet
import net.nomia.main.data.remote.RemoteUpdateTerminalService
import net.nomia.pos.core.handler.OnApplicationCreated
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class MainModule {

    @Singleton
    @Provides
    @IntoSet
    fun provideOnApplicationCreatedHandler(
        remoteUpdateTerminalService: RemoteUpdateTerminalService
    ): OnApplicationCreated {
        return OnApplicationCreated {
            remoteUpdateTerminalService.enableSync(true)
        }
    }
}
