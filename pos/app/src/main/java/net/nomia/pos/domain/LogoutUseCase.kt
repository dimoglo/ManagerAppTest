package net.nomia.pos.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.nomia.main.config.MainDatabase
import net.nomia.settings.config.SettingsDatabase

interface LogoutUseCase {
    suspend fun invoke()
}

class LogoutUseCaseImpl(
    private val settingsDatabase: SettingsDatabase,
    private val mainDatabase: MainDatabase,
) : LogoutUseCase {

    override suspend fun invoke() {
        withContext(Dispatchers.IO) {
            settingsDatabase.settingsDao().deleteAll()
            settingsDatabase.terminalDao().deleteAll()

            mainDatabase.clearAllTables()
        }
    }
}
