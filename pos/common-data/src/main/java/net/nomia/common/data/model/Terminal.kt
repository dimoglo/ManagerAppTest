package net.nomia.common.data.model

import java.util.UUID

data class Terminal(
    val id: ID,
    var name: String,
    val organization: Organization,
    val storeId: Store.ID,
    var menu: Menu?,
    var orderSequence: Long,
    val deviceType: DeviceType,
) {
    @JvmInline
    value class ID(val value: UUID)

    enum class DeviceType {
        Phone,
        Tablet,
        Unknown,
    }
}
