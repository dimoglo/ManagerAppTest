package net.nomia.settings.data.local.entity

import androidx.room.ColumnInfo
import java.util.Currency
import java.util.UUID

data class OrganizationData(
    val id: UUID,
    val name: String?,
    val code: String,
    @ColumnInfo(defaultValue = "RUB")
    val currency: Currency,
)
