package net.nomia.pos.ui.auth.pos_setup.mvi


import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.text.Content
import net.nomia.pos.domain.auth.pos_setup.TerminalState
import net.nomia.settings.domain.model.ServerProvider

internal sealed interface PosSetupMviEffect {

    data class SetOrganizations(val response: Response<out List<Organization>>) : PosSetupMviEffect

    data class SetStores(
        val organization: Organization,
        val response: Response<out List<Store>>
    ) : PosSetupMviEffect

    data class SetMenus(
        val store: Store,
        val response: Response<out List<Menu>>
    ) : PosSetupMviEffect

    data class SetMenu(val menu: Menu) : PosSetupMviEffect
    data class SetTerminalName(val name: String) : PosSetupMviEffect
    data class SaveTerminal(val state: TerminalState) : PosSetupMviEffect

    data class RestoreTerminal(val terminal: Terminal?) : PosSetupMviEffect
    data class Error(val content: Content?) : PosSetupMviEffect
    data class SetServerProvider(val serverProvider: ServerProvider) : PosSetupMviEffect

    object Logout : PosSetupMviEffect

    data class OpenErpExternally(val serverProvider: ServerProvider) : PosSetupMviEffect

}
