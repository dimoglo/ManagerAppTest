package net.nomia.pos.ui.auth.pos_setup.mvi


import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import net.nomia.common.data.model.Organization
import net.nomia.mvi.MviActor
import net.nomia.pos.domain.auth.pos_setup.use_cases.MenusUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.OrganizationsUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.RestoreTerminalUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.SaveTerminalUseCase
import net.nomia.pos.domain.auth.pos_setup.use_cases.StoresUseCase
import net.nomia.pos.domain.auth.pos_setup.EnabledList
import net.nomia.pos.domain.auth.pos_setup.TerminalValue
import net.nomia.pos.domain.LogoutUseCase

@Suppress("TooManyFunctions")
internal class PosSetupMviActor(
    private val organizationsUseCase: OrganizationsUseCase,
    private val storesUseCase: StoresUseCase,
    private val menusUseCase: MenusUseCase,
    private val saveTerminalUseCase: SaveTerminalUseCase,
    private val restoreTerminalUseCase: RestoreTerminalUseCase,
    private val logoutUseCase: LogoutUseCase,
) : MviActor<PosSetupMviAction, PosSetupMviEffect, PosSetupMviState> {

    override fun invoke(previousState: PosSetupMviState, action: PosSetupMviAction): Flow<PosSetupMviEffect> {
        return when (action) {
            is PosSetupMviAction.SelectOrganization -> action.invoke()
            is PosSetupMviAction.SelectStore -> action.invoke(previousState.terminalValue.organization?.id)
            is PosSetupMviAction.SelectMenu -> action.invoke()
            is PosSetupMviAction.ChangeTerminalName -> action.invoke()
            PosSetupMviAction.SaveTerminal -> finishTermialSetup(previousState.terminalValue)
            is PosSetupMviAction.Logout -> action.invoke()
            is PosSetupMviAction.ResetError -> flowOf(PosSetupMviEffect.Error(null))
            PosSetupMviAction.RetryMenusRequestIfFailed -> retryMenusRequestIfFailed(previousState)
            PosSetupMviAction.RetryOrganizationsRequestIfFailed -> retryOrganizationsRequestIfFailed(previousState)
            PosSetupMviAction.RetryStoresRequestIfFailed -> retryStoresRequestIfFailed(previousState)
            is PosSetupMviAction.NavigateToErp ->
                flowOf(PosSetupMviEffect.OpenErpExternally(previousState.serverProvider))
        }
    }

    private fun PosSetupMviAction.SelectOrganization.invoke(): Flow<PosSetupMviEffect> {
        return storesUseCase.get(organization.id)
            .map { storesResponse -> PosSetupMviEffect.SetStores(organization, storesResponse) }
    }

    private fun PosSetupMviAction.SelectStore.invoke(organizationId: Organization.ID?): Flow<PosSetupMviEffect> {
        return merge(
            menusUseCase.get(organizationId, store.id)
                .map { response -> PosSetupMviEffect.SetMenus(store, response) },
            restoreTerminalUseCase.restore(organizationId, store.id)
                .map { terminal -> PosSetupMviEffect.RestoreTerminal(terminal) }
        )
    }

    private fun PosSetupMviAction.SelectMenu.invoke(): Flow<PosSetupMviEffect> {
        return flowOf(PosSetupMviEffect.SetMenu(menu))
    }

    private fun PosSetupMviAction.ChangeTerminalName.invoke(): Flow<PosSetupMviEffect> {
        return flowOf(PosSetupMviEffect.SetTerminalName(name))
    }

    private fun PosSetupMviAction.Logout.invoke(): Flow<PosSetupMviEffect> = flow {
        logoutUseCase.invoke()
        emit(PosSetupMviEffect.Logout)
    }

    private fun finishTermialSetup(terminalValue: TerminalValue): Flow<PosSetupMviEffect> {
        return saveTerminalUseCase.save(terminalValue)
            .map { state -> PosSetupMviEffect.SaveTerminal(state) }
    }

    private inline fun <reified T> EnabledList<T>.retryIfFailed(
        action: () -> Flow<PosSetupMviEffect>
    ): Flow<PosSetupMviEffect> = if (isError) action.invoke() else emptyFlow()

    private fun retryOrganizationsRequestIfFailed(previousState: PosSetupMviState): Flow<PosSetupMviEffect> {
        return previousState.organizations.retryIfFailed {
            organizationsUseCase
                .get()
                .map { PosSetupMviEffect.SetOrganizations(it) }
        }
    }

    private fun retryStoresRequestIfFailed(previousState: PosSetupMviState): Flow<PosSetupMviEffect> {
        return previousState.stores.retryIfFailed {
            previousState.terminalValue.organization?.let { org ->
                return PosSetupMviAction.SelectOrganization(org).invoke()
            } ?: emptyFlow()
        }
    }

    private fun retryMenusRequestIfFailed(previousState: PosSetupMviState): Flow<PosSetupMviEffect> {
        return previousState.menus.retryIfFailed {
            previousState.terminalValue.store?.let { store ->
                PosSetupMviAction.SelectStore(store).invoke(previousState.terminalValue.organization?.id)
            } ?: emptyFlow()
        }
    }


}
