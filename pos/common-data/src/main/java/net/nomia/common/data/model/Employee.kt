package net.nomia.common.data.model

import java.util.UUID

class Employee(
    val id: ID,
    val accountId: AccountId? = null,
    val firstName: String,
    val lastName: String,
    val vatin: String? = null,
    val pin: Pin? = null,
    val groups: List<EmployeeGroup> = emptyList()
) {
    @JvmInline
    value class ID(val value: UUID)

    @JvmInline
    value class AccountId(val value: UUID)

    @JvmInline
    value class Pin(val value: String)

    val fullName: String
        get() = "$firstName $lastName"

}
