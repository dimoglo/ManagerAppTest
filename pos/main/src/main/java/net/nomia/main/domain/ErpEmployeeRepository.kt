package net.nomia.main.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.runBlocking
import net.nomia.common.data.model.Employee
import net.nomia.common.data.model.Organization
import net.nomia.common.data.model.Page
import net.nomia.common.data.model.PageRequest
import net.nomia.erp.api.ErpClientService
import net.nomia.erp.apiFlow
import net.nomia.erp.query.GetAllEmployeesPageableQuery
import net.nomia.erp.query.GetEmployeeByAccountIdAndOrganizationIdQuery
import net.nomia.erp.schema.toApi
import net.nomia.main.domain.model.Auth
import javax.inject.Inject

class ErpEmployeeRepository @Inject constructor(
    private val erpClientService: ErpClientService
) {

    @Throws
    suspend fun findAll(pageRequest: PageRequest): Page<Employee> {
        val client = erpClientService.applicationClient().filterNotNull().first()
        val response = client.query(GetAllEmployeesPageableQuery(pageRequest.toApi())).apiFlow().first()
        return response.data!!.allEmployeesPageable.let { page ->
            Page(
                content = page.content.map { it.fragments.employeeFragment.toDomain() },
                pageNumber = page.pageNumber,
                pageSize = page.pageSize,
                total = page.total,
                totalPages = page.totalPages
            )
        }
    }

    suspend fun findAll(pageSize: Int): Iterator<Page<Employee>> {
        return object : AbstractIterator<Page<Employee>>() {
            private var nextPage: Int = 0
            private var hasNext: Boolean = true
            override fun computeNext() {

                if (hasNext) {
                    val page = runBlocking {
                        findAll(PageRequest(page = nextPage, size = pageSize.toLong()))
                    }
                    setNext(page)
                    if (page.pageNumber + 1 < page.totalPages) {
                        nextPage = page.pageNumber + 1
                    } else {
                        hasNext = false
                    }
                } else {
                    done()
                }
            }
        }
    }

    fun findEmployeeByAccountIdAndOrganizationId(
        auth: Auth,
        accountId: Employee.AccountId,
        organizationId: Organization.ID
    ): Flow<Employee?> {
        return erpClientService.principalClient(
            token = auth.accessToken
        ).flatMapLatest { client ->
            client.query(
                GetEmployeeByAccountIdAndOrganizationIdQuery(
                    accountId = accountId.value,
                    organizationId = organizationId.value
                )
            ).apiFlow()
                .mapLatest { it.data?.employeeByAccountIdAndOrganizationId?.fragments?.employeeFragment?.toDomain() }
        }
    }
}
