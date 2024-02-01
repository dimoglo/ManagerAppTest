package net.nomia.settings.domain

import kotlinx.coroutines.flow.Flow
import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal
import net.nomia.common.data.model.Theme
import net.nomia.settings.domain.model.AppearanceData
import net.nomia.settings.domain.model.ApplicationToken
import net.nomia.settings.domain.model.MenuAppearance
import net.nomia.settings.domain.model.ServerProvider
import java.time.Instant
import java.util.Currency

interface SettingsRepository {
    fun getTerminal(): Flow<Terminal?>
    suspend fun saveTerminal(terminal: Terminal)
    suspend fun setDefaultTerminal(terminal: Terminal)
    fun getServerProvider(): Flow<ServerProvider>
    suspend fun saveServerProvider(serverProvider: ServerProvider)
    fun getApplicationToken(): Flow<ApplicationToken?>
    suspend fun saveApplicationToken(applicationToken: ApplicationToken)
    suspend fun deleteAll()

    fun getDiscountLatestDateSync(): Flow<Instant?>
    suspend fun saveDiscountLatestDateSync(date: Instant)

    fun getMenuId(): Flow<Menu.ID?>
    fun getOrganizationId(): Flow<Organization.ID?>
    fun getStoreId(): Flow<Store.ID?>
    fun getCurrency(): Flow<Currency?>

    fun getAppearance(): Flow<AppearanceData>
    fun getMenuAppearance(): Flow<MenuAppearance>

    suspend fun setTheme(theme: Theme)
    suspend fun setShowSectionImages(showImages: Boolean)
    suspend fun setShowItemImages(enabledItemImages: Boolean)
    suspend fun setSectionColors(enabledSectionColors: Boolean)
    suspend fun setUseCustomTheme(useCustomTheme: Boolean)

    suspend fun incrementOrderSequence(terminalId: Terminal.ID): String
}
