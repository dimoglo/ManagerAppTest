package net.nomia.pos.domain.auth.pos_setup.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal
import net.nomia.main.domain.ErpTerminalRepository
import net.nomia.settings.domain.SettingsRepository
import javax.inject.Inject

interface RestoreTerminalUseCase {

    fun restore(organizationId: Organization.ID?, storeId: Store.ID?) : Flow<Terminal?>
}

class RestoreTerminalUseCaseImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val terminalRepository: ErpTerminalRepository,
    private val settingsRepository: SettingsRepository
) : RestoreTerminalUseCase {

    override fun restore(organizationId: Organization.ID?, storeId: Store.ID?): Flow<Terminal?> =
        combine(flowOf(organizationId), flowOf(storeId)) { orgId, storeId ->
            val auth = authUseCase.get()
            if (auth != null && orgId != null && storeId != null) {
                terminalRepository.findTerminalByFingerprintAndStoreId(auth, orgId, storeId)
            } else {
                settingsRepository.getTerminal()
            }
                .distinctUntilChanged()
                .firstOrNull()
        }

}
