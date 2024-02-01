package net.nomia.main.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import net.nomia.common.data.Constants
import net.nomia.common.data.model.Employee
import net.nomia.main.domain.model.Auth
import net.nomia.main.domain.model.Principal
import net.nomia.settings.domain.SettingsRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrincipalRepository @Inject constructor(
    private val scope: CoroutineScope,
    private val erpLoginRepository: ErpLoginRepository,
    private val erpEmployeeRepository: ErpEmployeeRepository,
    private val settingsRepository: SettingsRepository
) {

    private val principal = MutableStateFlow<Principal?>(null)
    val currentPrincipal = principal.asStateFlow()

    @Suppress("MagicNumber")
    fun login(employee: Employee) {
        principal.value = Principal.InternalUser(
            employee = flowOf(employee).stateIn(scope, Constants.SharingStartedWithDefaultTimeout, null)
        )
    }

    fun login(auth: Auth) {
        val authFlow = erpLoginRepository.watchRefreshToken(auth)
            .stateIn(scope, SharingStarted.Eagerly, auth)

        principal.value = Principal.ExternalUser(
            auth = authFlow,
            employee = authFlow.combineTransform(settingsRepository.getOrganizationId()) { latestAuth, orgId ->
                val accountId = latestAuth.accessToken
                    .getClaim("id")
                    .asObject(UUID::class.java)
                    ?.let(Employee::AccountId)

                if (accountId != null && orgId != null) {
                    emitAll(
                        erpEmployeeRepository.findEmployeeByAccountIdAndOrganizationId(
                            auth = latestAuth,
                            accountId = accountId,
                            organizationId = orgId
                        )
                    )
                }
            }.stateIn(scope, SharingStarted.Eagerly, null)
        )
    }

    fun lock() {
        principal.value = null
    }
}
