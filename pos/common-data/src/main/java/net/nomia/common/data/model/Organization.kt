package net.nomia.common.data.model

import java.util.Currency
import java.util.UUID

class Organization(
    val id: ID,
    var name: String?,
    val code: Code,
    val currency: Currency,
) {

    @JvmInline
    value class ID(val value: UUID)

    @JvmInline
    value class Code(val value: String)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Organization) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }


}
