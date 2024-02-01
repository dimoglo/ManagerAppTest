package net.nomia.main.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.transformLatest
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Store
import net.nomia.common.ui.R
import net.nomia.erp.api.ErpClientService
import net.nomia.erp.apiFlow
import net.nomia.erp.query.GetAllStoresQuery
import net.nomia.main.domain.model.Auth
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.text.Content
import javax.inject.Inject

class ErpStoreRepository @Inject constructor(
    private val erpClientService: ErpClientService
) {

    fun findAll(auth: Auth, organizationId: Organization.ID): Flow<Response<out List<Store>>> {
        return erpClientService.principalClient(
            token = auth.accessToken,
            organizationId = organizationId
        ).take(1)
            .transformLatest { client ->
                emit(Response.Loading)
                client.query(GetAllStoresQuery())
                    .apiFlow()
                    .mapLatest { response ->
                        response.data!!.allStores.map { it.fragments.mainStoreFragment.toDomain() }
                    }.collectLatest { emit(Response.Success(it)) }
            }.catch { e ->
                emit(
                    Response.Error(e.message?.let { Content.Text(it) } ?: Content.ResValue(R.string.unknown_error), e)
                )
            }
    }
}
