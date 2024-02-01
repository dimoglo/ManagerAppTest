package net.nomia.pos.domain.auth.pos_setup.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import net.nomia.common.data.model.Organization
import net.nomia.main.domain.ErpOrganizationRepository
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.text.Content
import javax.inject.Inject

interface OrganizationsUseCase {

    fun observe() : Flow<Response<out List<Organization>>>

    fun get() : Flow<Response<out List<Organization>>>
}

class OrganizationsUseCaseImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val erpOrganizationRepository: ErpOrganizationRepository,
) : OrganizationsUseCase {

    override fun observe(): Flow<Response<out List<Organization>>> = authUseCase.observe()
        .distinctUntilChanged()
        .flatMapLatest { auth ->
            if (auth != null) {
                erpOrganizationRepository.findAll(auth)
            } else {
                flowOf(Response.Error(Content.Text("Auth is null")))
            }
        }

    override fun get(): Flow<Response<out List<Organization>>> = flow {
        val auth = authUseCase.get()
        if (auth != null) {
            emitAll(erpOrganizationRepository.findAll(auth))
        } else {
            emit(Response.Success(emptyList()))
        }
    }

}
