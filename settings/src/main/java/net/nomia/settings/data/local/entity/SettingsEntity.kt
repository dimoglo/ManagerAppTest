package net.nomia.settings.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import net.nomia.common.data.model.Theme
import net.nomia.settings.data.toEntity
import net.nomia.settings.domain.model.ServerProvider
import java.time.Instant
import java.util.UUID

@Entity(tableName = "settings")
data class SettingsEntity(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),
    var terminalId: UUID? = null,
    @Embedded(prefix = "server_provider_")
    var serverProvider: ServerProviderData = ServerProvider.DEFAULT.toEntity(),
    @Embedded(prefix = "application_token_")
    var applicationToken: ApplicationTokenData? = null,
    var discountLatestSync: Instant? = null,
    @ColumnInfo(defaultValue = "0")
    val useCustomTheme: Boolean = false,
    @ColumnInfo(defaultValue = "LIGHT")
    val customTheme: Theme = Theme.LIGHT,
    @ColumnInfo(defaultValue = "0")
    val showSectionImages: Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val showItemImages: Boolean = false,
    @ColumnInfo(defaultValue = "0")
    val showSectionColors: Boolean = false,
)
