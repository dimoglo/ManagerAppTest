package net.nomia.main.domain.model

import net.nomia.common.data.model.Menu

data class TerminalUpdateInput(
    val name: String,
    val menuId: Menu.ID,
    val orderSequence: Long
)
