package net.nomia.main.domain

import android.annotation.SuppressLint
import android.content.Context
import android.provider.Settings.Secure
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.take
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.data.model.Terminal
import net.nomia.erp.api.ErpClientService
import net.nomia.erp.apiFlow
import net.nomia.erp.mutation.SaveTerminalDataMutation
import net.nomia.erp.query.FindTerminalByFingerprintAndStoreIdQuery
import net.nomia.erp.schema.type.TerminalInputData
import net.nomia.main.domain.model.Auth
import net.nomia.main.domain.model.TerminalInput
import net.nomia.main.domain.model.TerminalUpdateInput
import net.nomia.settings.getDeviceType
import net.nomia.settings.domain.SettingsRepository
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErpTerminalRepository @Inject constructor(
    private val erpClientService: ErpClientService,
    private val settingsRepository: SettingsRepository,
    @ApplicationContext private val context: Context,
) {

    companion object {
        private const val TAG = "ErpTerminalRepository"
    }

    @SuppressLint("HardwareIds")
    private val deviceId: String = Secure.getString(context.contentResolver, Secure.ANDROID_ID)


    fun findTerminalByFingerprintAndStoreId(
        auth: Auth,
        organizationId: Organization.ID,
        storeId: Store.ID
    ): Flow<Terminal?> {
        return erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = organizationId
        ).take(1)
            .flatMapLatest { client ->
                client.query(FindTerminalByFingerprintAndStoreIdQuery(deviceId, storeId.value))
                    .apiFlow()
            }.mapLatest { response ->
                response.data
                    ?.findTerminalByFingerprintAndStoreId
                    ?.fragments
                    ?.terminalFragment
                    ?.toDomain(deviceType = context.getDeviceType())
            }.catch { e ->
                Timber.tag(TAG).e(e, "Failed to findTerminalByFingerprintAndStoreId")
                emit(null)
            }
    }

    fun save(terminal: TerminalInput, auth: Auth? = null): Flow<Terminal.ID> {

        val clientFlow = if (auth == null) {
            erpClientService.applicationClient().filterNotNull()
        } else {
            erpClientService.principalClient(
                token = auth.accessToken,
                organizationId = terminal.organization.id
            )
        }

        return clientFlow.take(1)
            .flatMapLatest { client ->
                client.mutate(
                    SaveTerminalDataMutation(
                        TerminalInputData(
                            fingerprint = deviceId,
                            storeId = terminal.store.id.value,
                            menuId = terminal.menu?.id?.value ?: UUID.randomUUID(),
                            name = terminal.name,
                            orderSequence = terminal.orderSequence
                        )
                    )
                ).apiFlow()
            }.mapLatest { Terminal.ID(it.data!!.saveTerminalData.id) }
    }

    suspend fun save(terminal: TerminalUpdateInput) {
        val client = erpClientService.applicationClient().filterNotNull().first()
        val storeId = settingsRepository.getStoreId().filterNotNull().first()

        client.mutate(
            SaveTerminalDataMutation(
                TerminalInputData(
                    fingerprint = deviceId,
                    storeId = storeId.value,
                    menuId = terminal.menuId.value,
                    name = terminal.name,
                    orderSequence = terminal.orderSequence
                )
            )
        ).apiFlow().collect()
    }
}
