package net.nomia.pos.di.auth.pos_setup

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import net.nomia.pos.domain.auth.pos_setup.use_cases.AuthUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.AuthUseCaseImpl
import net.nomia.pos.domain.auth.pos_setup.use_cases.MenusUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.MenusUseCaseImpl
import net.nomia.pos.domain.auth.pos_setup.use_cases.OrganizationsUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.OrganizationsUseCaseImpl
import net.nomia.pos.domain.auth.pos_setup.use_cases.RestoreTerminalUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.RestoreTerminalUseCaseImpl
import net.nomia.pos.domain.auth.pos_setup.use_cases.SaveTerminalUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.SaveTerminalUseCaseImpl
import net.nomia.pos.domain.auth.pos_setup.use_cases.ServerProviderUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.ServerProviderUseCaseImpl
import net.nomia.pos.domain.auth.pos_setup.use_cases.StoresUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.StoresUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface PosSetupUseCaseModule {

    @Binds
    fun bindAuthUseCase(
        authUseCase: AuthUseCaseImpl,
    ): AuthUseCase

    @Binds
    fun bindOrganizationsUseCase(
        organizationsUseCase: OrganizationsUseCaseImpl,
    ): OrganizationsUseCase

    @Binds
    fun bindStoresUseCase(
        storesUseCase: StoresUseCaseImpl,
    ): StoresUseCase

    @Binds
    fun bindMenusUseCase(
        menusUseCase: MenusUseCaseImpl,
    ): MenusUseCase

    @Binds
    fun bindSaveTerminalUseCase(
        saveTerminalUseCase: SaveTerminalUseCaseImpl,
    ): SaveTerminalUseCase

    @Binds
    fun bindRestoreTerminalUseCase(
        restoreTerminalUseCase: RestoreTerminalUseCaseImpl,
    ): RestoreTerminalUseCase

    @Binds
    fun bindServerProviderUseCase(
        serverProviderUseCase: ServerProviderUseCaseImpl,
    ): ServerProviderUseCase

}
