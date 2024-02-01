package net.nomia.common.data.model

import java.util.UUID

class Menu(
    val id: ID,
    var name: String,
) {
    @JvmInline
    value class ID(val value: UUID)

    data class MenuItemsAmount(
        val inactive: Long,
        val total: Long
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Menu) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}
