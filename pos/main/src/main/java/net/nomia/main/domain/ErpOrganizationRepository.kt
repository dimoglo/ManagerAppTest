package net.nomia.main.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.transformLatest
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Page
import net.nomia.common.data.model.PageRequest
import net.nomia.common.ui.R
import net.nomia.erp.api.ErpClientService
import net.nomia.erp.apiFlow
import net.nomia.erp.query.GetAllOrganizationsQuery
import net.nomia.erp.schema.toApi
import net.nomia.erp.schema.type.PageRequestInput
import net.nomia.main.domain.model.Auth
import net.nomia.pos.core.data.Response
import net.nomia.pos.core.text.Content
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ErpOrganizationRepository @Inject constructor(
    private val erpClientService: ErpClientService
) {
    @Throws
    suspend fun findAll(auth: Auth, pageRequest: PageRequest): Page<Organization> {
        val client = erpClientService.principalClient(token = auth.accessToken).first()
        val response = client.query(GetAllOrganizationsQuery(pageRequest.toApi())).apiFlow().first()

        return response.data!!.allOrganizationsPageable.let { page ->
            Page(
                content = page.content.map { it.fragments.organizationFragment.toDomain() },
                pageNumber = page.pageNumber,
                pageSize = page.pageSize,
                total = page.total,
                totalPages = page.totalPages
            )
        }
    }


    fun findAll(auth: Auth): Flow<Response<out List<Organization>>> {
        return erpClientService.principalClient(token = auth.accessToken)
            .transformLatest { client ->
                emit(Response.Loading)
                client.query(
                    GetAllOrganizationsQuery(
                        PageRequestInput(
                            page = 0,
                            size = 50
                        )
                    )
                ).apiFlow()
                    .mapLatest {
                        it.data!!.allOrganizationsPageable.content.map { it.fragments.organizationFragment.toDomain() }
                    }
                    .collectLatest { emit(Response.Success(it)) }
            }.catch { e ->
                emit(Response.Error(e.message?.let { Content.Text(it) } ?: Content.ResValue(R.string.unknown_error)))
            }
    }
}
