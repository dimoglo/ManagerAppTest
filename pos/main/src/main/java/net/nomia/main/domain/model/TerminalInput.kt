package net.nomia.main.domain.model

import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal

data class TerminalInput(
    var id: Terminal.ID? = null,
    val name: String,
    val organization: Organization,
    val store: Store,
    val menu: Menu?,
    val orderSequence: Long = 0
)
