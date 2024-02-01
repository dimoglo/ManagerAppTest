package net.nomia.settings.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import net.nomia.settings.data.local.entity.SettingsEntity
import net.nomia.settings.data.local.entity.TerminalEntity
import net.nomia.settings.domain.model.AppearanceData
import net.nomia.settings.domain.model.MenuAppearance
import java.util.Currency
import java.util.UUID

@Dao
interface SettingsDao {

    @Transaction
    @Query("SELECT * FROM settings LIMIT 1")
    fun findFirst(): Flow<SettingsEntity?>

    @Transaction
    @Query(
        """
        SELECT t.*
        FROM terminal AS t
        INNER JOIN settings AS s ON s.terminalId = t.id
        LIMIT 1
    """
    )
    fun findDefaultTerminal(): Flow<TerminalEntity?>

    @Transaction
    @Query(
        """
        SELECT t.menu_id
        FROM terminal AS t
        INNER JOIN settings AS s ON s.terminalId = t.id
        LIMIT 1
    """
    )
    fun findMenuId(): Flow<UUID?>

    @Transaction
    @Query(
        """
        SELECT t.organization_id
        FROM terminal AS t
        INNER JOIN settings AS s ON s.terminalId = t.id
        LIMIT 1
    """
    )
    fun findOrganizationId(): Flow<UUID?>

    @Transaction
    @Query(
        """
        SELECT t.storeId
        FROM terminal AS t
        INNER JOIN settings AS s ON s.terminalId = t.id
        LIMIT 1
    """
    )
    fun findStoreId(): Flow<UUID?>

    @Transaction
    @Query(
        """
        SELECT t.organization_currency
        FROM terminal AS t
        INNER JOIN settings AS s ON s.terminalId = t.id
        LIMIT 1
    """
    )
    fun findCurrency(): Flow<Currency?>

    @Query(
        """
        SELECT
            s.customTheme AS theme,
            s.useCustomTheme AS useCustomTheme,
            s.showSectionImages AS showSectionImages,
            s.showItemImages AS showItemImages,
            s.showSectionColors AS showSectionColors
        FROM settings AS s
        LIMIT 1
    """
    )
    fun getAppearance(): Flow<AppearanceData?>


    @Query(
        """
            SELECT 
                s.showSectionColors AS enabledSectionColors,
                s.showSectionImages AS enabledSectionImages,
                s.showItemImages AS enabledItemImages
            FROM settings AS s
            LIMIT 1
        """
    )
    fun getMenuAppearance(): Flow<MenuAppearance>

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(vararg entities: SettingsEntity)

    @Transaction
    @Delete
    suspend fun delete(vararg entities: SettingsEntity)

    @Transaction
    @Query("DELETE FROM settings")
    suspend fun deleteAll()
}
