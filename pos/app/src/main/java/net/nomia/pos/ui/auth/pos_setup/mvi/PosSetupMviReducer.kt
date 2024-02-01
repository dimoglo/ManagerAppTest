package net.nomia.pos.ui.auth.pos_setup.mvi


import net.nomia.mvi.MviReducer
import net.nomia.pos.R
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.text.Content
import net.nomia.pos.domain.auth.pos_setup.EnabledList
import net.nomia.pos.domain.auth.pos_setup.TerminalState
import net.nomia.pos.domain.auth.pos_setup.emptyEnabledList

internal object PosSetupMviReducer : MviReducer<PosSetupMviEffect, PosSetupMviState> {

    override suspend fun invoke(previousState: PosSetupMviState, effect: PosSetupMviEffect): PosSetupMviState {
        return when (effect) {
            is PosSetupMviEffect.SetOrganizations -> effect.invoke(previousState)
            is PosSetupMviEffect.SetServerProvider -> previousState.copy(serverProvider = effect.serverProvider)
            is PosSetupMviEffect.SetStores -> effect.invoke(previousState)
            is PosSetupMviEffect.SetMenus -> effect.invoke(previousState)
            is PosSetupMviEffect.SetMenu -> effect.invoke(previousState)
            is PosSetupMviEffect.SetTerminalName -> effect.invoke(previousState)
            is PosSetupMviEffect.SaveTerminal -> effect.invoke(previousState)
            is PosSetupMviEffect.RestoreTerminal -> effect.invoke(previousState)
            is PosSetupMviEffect.Error -> effect.invoke(previousState)
            is PosSetupMviEffect.Logout -> previousState
            is PosSetupMviEffect.OpenErpExternally -> previousState
        }
    }

    private fun PosSetupMviEffect.SetOrganizations.invoke(previousState: PosSetupMviState): PosSetupMviState {
        val organizations = response.toEnabledList()
        val hasCurrentOrganization = organizations.contains(previousState.terminalValue.organization)
        return previousState.copy(
            terminalValue = if (hasCurrentOrganization) {
                previousState.terminalValue
            } else {
                previousState.terminalValue.copy(organization = null, store = null, menu = null)
            },
            organizations = organizations,
            stores = if (hasCurrentOrganization) previousState.stores else emptyEnabledList(false),
            menus = if (hasCurrentOrganization) previousState.menus else emptyEnabledList(false),
            hasNoMenusCreated = if (hasCurrentOrganization) previousState.hasNoMenusCreated else false,
            error = if (organizations.isError) {
                Content.ResValue(R.string.pos_setup_loading_error)
            } else {
                previousState.error
            },
        )
    }

    private fun PosSetupMviEffect.SetStores.invoke(previousState: PosSetupMviState): PosSetupMviState {
        val stores = response.toEnabledList()
        val hasCurrentStore = stores.contains(previousState.terminalValue.store)
        return previousState.copy(
            terminalValue = if (hasCurrentStore) {
                previousState.terminalValue.copy(organization = organization)
            } else {
                previousState.terminalValue.copy(organization = organization, store = null, menu = null)
            },
            stores = stores,
            menus = if (hasCurrentStore) previousState.menus else emptyEnabledList(false),
            hasNoMenusCreated = if (hasCurrentStore) previousState.hasNoMenusCreated else false,
            error = if (stores.isError) {
                Content.ResValue(R.string.pos_setup_loading_error)
            } else {
                previousState.error
            },
        )
    }

    private fun PosSetupMviEffect.SetMenus.invoke(previousState: PosSetupMviState): PosSetupMviState {
        val menus = response.toEnabledList()
        val hasCurrentMenu= menus.contains(previousState.terminalValue.menu)
        return previousState.copy(
            terminalValue = if (hasCurrentMenu) {
                previousState.terminalValue.copy(store = store)
            } else {
                previousState.terminalValue.copy(store = store, menu = null)
            },
            menus = menus,
            hasNoMenusCreated = menus.run { !isError && isEmpty() } && response !is Response.Loading,
            error = if (menus.isError) {
                Content.ResValue(R.string.pos_setup_loading_error)
            } else {
                previousState.error
            },
        )
    }

    private fun PosSetupMviEffect.RestoreTerminal.invoke(previousState: PosSetupMviState): PosSetupMviState {
        return if (terminal != null) {
            previousState.copy(
                terminalValue = previousState.terminalValue.copy(
                    id = terminal.id,
                    name = terminal.name,
                    menu = terminal.menu,
                    orderSequence = terminal.orderSequence,
                    mustUpdateName = true
                ),
            )
        } else {
            previousState
        }
    }

    private fun <T> Response<out List<T>>.toEnabledList() : EnabledList<T> {
        return when (this) {
            Response.Loading -> emptyEnabledList(true)
            is Response.Success -> EnabledList(result, result.isNotEmpty(), isError = false)
            else -> emptyEnabledList(enabled = true , isError = true)
        }
    }

    private fun PosSetupMviEffect.SetMenu.invoke(previousState: PosSetupMviState): PosSetupMviState {
        return previousState.copy(terminalValue = previousState.terminalValue.copy(menu = menu))
    }

    private fun PosSetupMviEffect.SetTerminalName.invoke(previousState: PosSetupMviState): PosSetupMviState {
        return previousState.copy(
            terminalValue = previousState.terminalValue.copy(name = name, mustUpdateName = false)
        )
    }

    private fun PosSetupMviEffect.SaveTerminal.invoke(previousState: PosSetupMviState): PosSetupMviState {
        return previousState.copy(
            terminalState = state,
            error = if (state is TerminalState.Error) {
                Content.ResValue(R.string.pos_setup_loading_error)
            } else {
                previousState.error
            }
        )
    }

    private fun PosSetupMviEffect.Error.invoke(previousState: PosSetupMviState): PosSetupMviState {
        return previousState.copy(error = content)
    }

}
