package net.nomia.pos.ui.auth.pos_setup.mvi

import net.nomia.mvi.Event

internal sealed interface PosSetupMviEvent : Event {

    object OnTerminalSaved : PosSetupMviEvent

    object OnLoggedOut : PosSetupMviEvent

    class OnOpenedErpExternally(val erpUri: String) : PosSetupMviEvent

}
