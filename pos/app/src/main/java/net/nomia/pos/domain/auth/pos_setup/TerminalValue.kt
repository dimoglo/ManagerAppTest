package net.nomia.pos.domain.auth.pos_setup

import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal

data class TerminalValue(
    val id: Terminal.ID? = null,
    val name: String = "",
    val organization: Organization? = null,
    val store: Store? = null,
    val menu: Menu? = null,
    val orderSequence: Long = 0,
    val mustUpdateName: Boolean = false
)
