package net.nomia.main.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class EmployeeFullDBView(
    @Embedded
    val employee: EmployeeEntity,

    @Relation(
        entity = EmployeeGroupEntity::class,
        parentColumn = "id",
        entityColumn = "employeeId"
    )
    val employeeGroups: List<EmployeeGroupEntity>
)
