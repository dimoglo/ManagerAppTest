package net.nomia.main.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import net.nomia.common.data.model.RoleCode
import java.util.UUID

@Entity(
    tableName = "employee_group",
    indices = [Index("employeeId")],
    foreignKeys = [ForeignKey(
        entity = EmployeeEntity::class,
        parentColumns = ["id"],
        childColumns = ["employeeId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class EmployeeGroupEntity(
    @PrimaryKey
    val id: UUID,
    val employeeId: UUID,
    val storeId: UUID?,
    val roleCode: RoleCode
)
