package net.nomia.common.data.model

import java.time.Instant
import java.util.UUID

data class Store(
    val id: ID,
    var name: String,
    var address: String?,
    val tablesFeatureEnabled: Boolean,
    val tablesLatestSync: Instant? = null
) {

    @JvmInline
    value class ID(val value: UUID)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Store) return false

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
