package net.nomia.pos.domain.auth.pos_setup

sealed class TerminalState {
    object Empty : TerminalState()
    object Processing : TerminalState()
    data class Error(val message: String) : TerminalState()
    object Saved : TerminalState()
}
