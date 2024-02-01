package net.nomia.main.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "employee")
data class EmployeeEntity(
    @PrimaryKey
    val id: UUID,
    val accountId: UUID?,
    val firstName: String,
    val lastName: String,
    val vatin: String?,
    val pin: String?
)
