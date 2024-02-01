package net.nomia.main.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transformLatest
import net.nomia.common.data.model.Menu
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.ui.R
import net.nomia.erp.api.ErpClientService
import net.nomia.erp.apiFlow
import net.nomia.erp.query.GetAllMenuQuery
import net.nomia.main.domain.model.Auth
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.text.Content
import javax.inject.Inject

class ErpMenuRepository @Inject constructor(
    private val erpClientService: ErpClientService
) {
    fun findAll(
        auth: Auth,
        organizationId: Organization.ID,
        storeId: Store.ID
    ): Flow<Response<out List<Menu>>> {
        return erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = organizationId
        ).transformLatest { client ->
            emit(Response.Loading)
            client.query(GetAllMenuQuery(storeId.value)).apiFlow()
                .mapLatest { it.data!!.allMenus?.filterNotNull() ?: emptyList() }
                .mapLatest { response ->
                    response.map { it.fragments.menuFragment.toDomain() }
                }.collectLatest { emit(Response.Success(it)) }
        }.catch { e ->
            emit(
                Response.Error(e.message?.let { Content.Text(it) } ?: Content.ResValue(R.string.unknown_error))
            )
        }
    }

    fun findAll(storeId: Store.ID): Flow<Response<out List<Menu>>> {
        return erpClientService.applicationClient().filterNotNull()
            .transformLatest { client ->
                emit(Response.Loading)
                client.query(GetAllMenuQuery(storeId.value)).apiFlow()
                    .mapLatest { it.data!!.allMenus?.filterNotNull() ?: emptyList() }
                    .mapLatest { response -> response.map { it.fragments.menuFragment.toDomain() } }
                    .collectLatest { emit(Response.Success(it)) }
            }.catch { e ->
                emit(
                    Response.Error(e.message?.let { Content.Text(it) } ?: Content.ResValue(R.string.unknown_error))
                )
            }
    }
}
