package net.nomia.pos.ui.auth.pos_setup.mvi

import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store


internal sealed interface PosSetupMviAction {

    data class SelectOrganization(val organization: Organization) : PosSetupMviAction

    data class SelectStore(val store: Store) : PosSetupMviAction

    data class SelectMenu(val menu: Menu) : PosSetupMviAction

    data class ChangeTerminalName(val name: String) : PosSetupMviAction

    object SaveTerminal : PosSetupMviAction

    object Logout : PosSetupMviAction

    object ResetError : PosSetupMviAction

    object RetryOrganizationsRequestIfFailed : PosSetupMviAction

    object RetryStoresRequestIfFailed : PosSetupMviAction

    object RetryMenusRequestIfFailed : PosSetupMviAction

    object NavigateToErp : PosSetupMviAction

}
