package net.nomia.settings.data

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal
import net.nomia.common.data.model.Theme
import net.nomia.settings.config.SettingsDatabase
import net.nomia.settings.data.local.entity.SettingsEntity
import net.nomia.settings.domain.SettingsRepository
import net.nomia.settings.domain.model.AppearanceData
import net.nomia.settings.domain.model.ApplicationToken
import net.nomia.settings.domain.model.MenuAppearance
import net.nomia.settings.domain.model.ServerProvider
import java.time.Instant
import java.util.Currency
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDatabase: SettingsDatabase
) : SettingsRepository {

    private val dao = settingsDatabase.settingsDao()

    override fun getTerminal(): Flow<Terminal?> {
        return dao.findDefaultTerminal()
            .distinctUntilChanged()
            .map { it?.toDomain() }
    }

    override suspend fun saveTerminal(terminal: Terminal) {
        settingsDatabase.terminalDao().save(terminal.toEntity())
    }

    override suspend fun setDefaultTerminal(terminal: Terminal) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(terminalId = terminal.id.value))
        }
    }

    override fun getServerProvider(): Flow<ServerProvider> {
        return getSettings().mapLatest { it.serverProvider }
            .distinctUntilChanged()
            .map { it.toDomain() }
    }

    override suspend fun saveServerProvider(serverProvider: ServerProvider) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(serverProvider = serverProvider.toEntity()))
        }
    }

    override fun getApplicationToken(): Flow<ApplicationToken?> {
        return getSettings().map { it.applicationToken }
            .distinctUntilChanged()
            .map { it?.toDomain() }
    }

    override suspend fun saveApplicationToken(applicationToken: ApplicationToken) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(applicationToken = applicationToken.toEntity()))
        }
    }

    override suspend fun deleteAll() {
        dao.deleteAll()
    }

    override fun getDiscountLatestDateSync() =
        getSettings().map { it.discountLatestSync }

    override suspend fun saveDiscountLatestDateSync(date: Instant) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(discountLatestSync = date))
        }
    }

    override fun getMenuId(): Flow<Menu.ID?> =
        dao.findMenuId().distinctUntilChanged().mapLatest { it?.let(Menu::ID) }

    override fun getOrganizationId(): Flow<Organization.ID?> =
        dao.findOrganizationId().distinctUntilChanged().mapLatest { it?.let(Organization::ID) }

    override fun getStoreId(): Flow<Store.ID?> =
        dao.findStoreId().distinctUntilChanged().mapLatest { it?.let(Store::ID) }

    override fun getCurrency(): Flow<Currency?> = dao.findCurrency().distinctUntilChanged()

    override fun getAppearance(): Flow<AppearanceData> =
        dao.getAppearance()
            .map { appearanceView ->
                appearanceView
                    ?: AppearanceData(
                        theme = Theme.LIGHT,
                        useCustomTheme = false,
                        showSectionImages = false,
                        showItemImages = false,
                        showSectionColors = false,
                    )
            }
            .distinctUntilChanged()

    override fun getMenuAppearance(): Flow<MenuAppearance> =
        dao.getMenuAppearance().distinctUntilChanged()

    override suspend fun setTheme(theme: Theme) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(customTheme = theme))
        }
    }

    override suspend fun setShowSectionImages(showImages: Boolean) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(showSectionImages = showImages))
        }
    }

    override suspend fun setShowItemImages(enabledItemImages: Boolean) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(showItemImages = enabledItemImages))
        }
    }

    override suspend fun setSectionColors(enabledSectionColors: Boolean) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(showSectionColors = enabledSectionColors))
        }
    }

    override suspend fun setUseCustomTheme(useCustomTheme: Boolean) {
        updateSettings { currentSettings ->
            dao.save(currentSettings.copy(useCustomTheme = useCustomTheme))
        }
    }

    override suspend fun incrementOrderSequence(terminalId: Terminal.ID): String {
        // TODO: 24.01.2022 Plus terminalId and storeId to orderSequence
        return settingsDatabase.withTransaction {
            val orderSequence = settingsDatabase.terminalDao().getOrderSequence(terminalId.value)
                .first() + 1
            settingsDatabase.terminalDao().saveOrderSequence(terminalId.value, orderSequence)
            orderSequence.toString()
        }
    }

    private suspend inline fun updateSettings(crossinline block: suspend (SettingsEntity) -> Unit) {
        settingsDatabase.withTransaction {
            val oldSettings = getSettings().first()
            block(oldSettings)
        }
    }

    private fun getSettings(): Flow<SettingsEntity> =
        dao.findFirst()
            .map { it ?: SettingsEntity() }
            .distinctUntilChanged()
}
