package net.nomia.pos.domain.auth.pos_setup.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flowOf
import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.main.domain.ErpMenuRepository
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.text.Content
import javax.inject.Inject

interface MenusUseCase {

    fun get(organizationId: Organization.ID?, storeId: Store.ID?) : Flow<Response<out List<Menu>>>
}

class MenusUseCaseImpl @Inject constructor(
    private val authUseCase: AuthUseCase,
    private val repository: ErpMenuRepository
) : MenusUseCase {

    override fun get(
        organizationId: Organization.ID?,
        storeId: Store.ID?,
    ): Flow<Response<out List<Menu>>> = combineTransform(
        flowOf(organizationId),
        flowOf(storeId)
    ) { orgId, storeId ->
        val auth = authUseCase.get()
        if (auth != null && orgId != null && storeId != null) {
            emitAll(repository.findAll(auth, orgId, storeId))
        } else {
            flowOf(Response.Error(Content.Text("Auth is null")))
        }
    }

}
