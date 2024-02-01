package net.nomia.pos.domain.auth.pos_setup.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import net.nomia.main.domain.PrincipalRepository
import net.nomia.main.domain.model.Auth
import net.nomia.main.domain.model.Principal
import javax.inject.Inject

interface AuthUseCase {

    fun observe() : Flow<Auth?>

    suspend fun get() : Auth?
}

class AuthUseCaseImpl @Inject constructor(
    principalRepository: PrincipalRepository,
) : AuthUseCase {

    private val authFlow = principalRepository
        .currentPrincipal
        .filterIsInstance<Principal.ExternalUser?>()
        .flatMapLatest { it?.auth ?: flowOf(null) }
        .distinctUntilChanged()

    override fun observe() = authFlow

    override suspend fun get(): Auth? = authFlow.firstOrNull()

}
