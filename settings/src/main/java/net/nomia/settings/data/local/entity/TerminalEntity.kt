package net.nomia.settings.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.nomia.common.data.model.Terminal
import java.util.UUID

@Entity(tableName = "terminal")
data class TerminalEntity(
    @PrimaryKey
    val id: UUID,
    val name: String,
    @Embedded(prefix = "organization_")
    val organization: OrganizationData,
    val storeId: UUID,
    @Embedded(prefix = "menu_")
    val menu: MenuData?,
    var orderSequence: Long,
    @ColumnInfo(defaultValue = "Unknown")
    val deviceType: Terminal.DeviceType,
)
