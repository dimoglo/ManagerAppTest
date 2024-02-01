package net.nomia.main.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import net.nomia.common.network.NetworkStatusTracker
import net.nomia.main.domain.ErpTerminalRepository
import net.nomia.main.domain.model.TerminalUpdateInput
import net.nomia.settings.domain.SettingsRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteUpdateTerminalService @Inject constructor(
    private val erpTerminalRepository: ErpTerminalRepository,
    settingsRepository: SettingsRepository,
    scope: CoroutineScope,
    networkStatusTracker: NetworkStatusTracker
) {
    private val enabled = MutableStateFlow(false)

    init {
        combine(
            networkStatusTracker.isAvailable,
            enabled,
            settingsRepository.getTerminal()
        ) { isNetworkAvailable, enabled, terminal ->
            if (isNetworkAvailable && terminal != null && enabled) {
                val menu = terminal.menu
                if (menu != null) {
                    val terminalValue = TerminalUpdateInput(
                        name = terminal.name,
                        menuId = menu.id,
                        orderSequence = terminal.orderSequence
                    )
                    Timber.tag(TAG).d("sync terminal: $terminalValue")
                    erpTerminalRepository.save(terminalValue)
                }
            }
        }
            .catch { e ->
                Timber.tag(TAG).w(e, "update terminal: ${e.message}")
            }
            .flowOn(Dispatchers.IO)
            .launchIn(scope)
    }

    fun enableSync(enabled: Boolean) {
        Timber.tag(TAG).d("enableSync: $enabled")
        this.enabled.tryEmit(enabled)
    }

    companion object {
        private const val TAG = "RemoteUpdateTerminalService"
    }
}
