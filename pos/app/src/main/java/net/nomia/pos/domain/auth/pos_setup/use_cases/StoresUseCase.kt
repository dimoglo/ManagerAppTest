package net.nomia.pos.domain.auth.pos_setup.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.main.domain.ErpStoreRepository
import net.nomia.pos.core.data.Response
import javax.inject.Inject

interface StoresUseCase {

    fun get(organizationId: Organization.ID) : Flow<Response<out List<Store>>>
}

class StoresUseCaseImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val erpStoreRepository: ErpStoreRepository,
) : StoresUseCase {

    override fun get(organizationId: Organization.ID): Flow<Response<out List<Store>>> = flow {
        val auth = authUseCase.get()
        if (auth != null) {
            emitAll(erpStoreRepository.findAll(auth, organizationId))
        } else {
            emit(Response.Success(emptyList()))
        }
    }

}
