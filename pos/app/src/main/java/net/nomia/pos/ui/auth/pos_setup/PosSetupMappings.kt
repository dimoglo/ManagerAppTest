package net.nomia.pos.ui.auth.pos_setup

import net.nomia.main.domain.model.TerminalInput
import net.nomia.pos.domain.auth.pos_setup.TerminalValue

@Suppress("ComplexCondition")
fun TerminalValue.toInput() =
    if (name.isNotBlank() && organization != null && store != null && menu != null) {
        TerminalInput(id, name, organization, store, menu, orderSequence)
    } else null
