package net.nomia.common.data.model

import java.util.UUID

class EmployeeGroup(
    val id: ID,
    val storeId: Store.ID?,
    val roleCode: RoleCode
) {
    @JvmInline
    value class ID(val value: UUID)
}
